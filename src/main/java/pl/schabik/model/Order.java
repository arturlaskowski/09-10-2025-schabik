package pl.schabik.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static pl.schabik.model.OrderStatus.PAID;
import static pl.schabik.model.OrderStatus.PENDING;


@Table(name = "orders")
@Entity
public class Order {

    @Id
    private UUID id;

    @NotNull
    private Instant createAt;

    @NotNull
    private Instant lastUpdateAt;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @NotNull
    @Min(0)
    private BigDecimal price;

    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToOne(cascade = CascadeType.ALL)
    private OrderAddress address;

    @NotNull
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    private List<OrderItem> items;

    @Version
    private int version;

    //For JPA
    protected Order() {
    }

    public Order(Customer customer, BigDecimal price, List<OrderItem> items, OrderAddress address) {
        this.customer = customer;
        this.price = price;
        this.items = items;
        this.address = address;
        initialize();
    }

    private void initialize() {
        this.id = UUID.randomUUID();
        this.createAt = Instant.now();
        this.lastUpdateAt = Instant.now();
        this.status = PENDING;
        validatePrice(price, items);
        initializeBasketItems();
    }

    private void validatePrice(BigDecimal price, List<OrderItem> items) {
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new OrderDomainException("Order price: " + price + " must be greater than zero");
        }

        BigDecimal itemsTotalCost = items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_EVEN);

        if (!price.equals(itemsTotalCost)) {
            throw new OrderDomainException("Total order price: " + price
                    + " is different than order items total: " + itemsTotalCost);
        }
    }

    private void initializeBasketItems() {
        int itemNumber = 1;
        for (OrderItem item : items) {
            item.initializeBasketItem(this, itemNumber++);
        }
    }

    public void pay() {
        if (PENDING != status) {
            throw new OrderDomainException("Order is not in correct state for pay operation");
        }
        lastUpdateAt = Instant.now();
        status = PAID;
    }

    public boolean isPendingStatus() {
        return PENDING == status;
    }

    public boolean isPaidStatus() {
        return PAID == status;
    }

    public UUID getId() {
        return id;
    }

    public Instant getCreateAt() {
        return createAt;
    }

    public Instant getLastUpdateAt() {
        return lastUpdateAt;
    }

    public Customer getCustomer() {
        return customer;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public OrderAddress getAddress() {
        return address;
    }

    public List<OrderItem> getItems() {
        return items;
    }
}
