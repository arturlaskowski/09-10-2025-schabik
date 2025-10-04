package pl.schabik.domain;

import java.time.Instant;
import java.util.List;

public class Order {

    private OrderId id;
    private Instant createAt;
    private Instant lastUpdateAt;
    private final Customer customer;
    private final Money price;
    private OrderStatus status;
    private final OrderAddress address;
    private final List<OrderItem> items;

    public Order(Customer customer, Money price, List<OrderItem> items, OrderAddress address) {
        this.customer = customer;
        this.price = price;
        this.items = items;
        this.address = address;
        initialize();
    }

    private void initialize() {
        this.id = OrderId.newOne();
        this.createAt = Instant.now();
        this.lastUpdateAt = Instant.now();
        this.status = OrderStatus.PENDING;
        validatePrice();
        initializeBasketItems();
    }

    public Order(OrderId id, Instant createAt, Instant lastUpdateAt, Customer customer, Money price, OrderStatus status, OrderAddress address, List<OrderItem> items) {
        this.id = id;
        this.createAt = createAt;
        this.lastUpdateAt = lastUpdateAt;
        this.customer = customer;
        this.price = price;
        this.status = status;
        this.address = address;
        this.items = items;
    }

    private void validatePrice() {
        Money itemsTotalCost = items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(Money.ZERO, Money::add);

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
        if (OrderStatus.PENDING != status) {
            throw new OrderDomainException("Order is not in correct state for pay operation");
        }
        lastUpdateAt = Instant.now();
        status = OrderStatus.PAID;
    }

    public boolean isPendingStatus() {
        return OrderStatus.PENDING == status;
    }

    public boolean isPaidStatus() {
        return OrderStatus.PAID == status;
    }

    public OrderId getId() {
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

    public Money getPrice() {
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
