package pl.schabik.infrastructure.persistence;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import pl.schabik.domain.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Table(name = "orders")
@Entity
public class JpaOrderEntity {

    @Id
    private UUID id;

    @NotNull
    private Instant createAt;

    @NotNull
    private Instant lastUpdateAt;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private JpaCustomerEntity customer;

    @NotNull
    @Column(name = "price")
    private BigDecimal price;

    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToOne(cascade = CascadeType.ALL)
    private JpaOrderAddressEntity address;

    @NotNull
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    private List<JpaOrderItemEntity> items;

    @Version
    private int version;

    protected JpaOrderEntity() {
    }

    public JpaOrderEntity(UUID id, Instant createAt, Instant lastUpdateAt, JpaCustomerEntity customer,
                          BigDecimal price, OrderStatus status, JpaOrderAddressEntity address, List<JpaOrderItemEntity> items) {
        this.id = id;
        this.createAt = createAt;
        this.lastUpdateAt = lastUpdateAt;
        this.customer = customer;
        this.price = price;
        this.status = status;
        this.address = address;
        this.items = items;
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

    public void setLastUpdateAt(Instant lastUpdateAt) {
        this.lastUpdateAt = lastUpdateAt;
    }

    public JpaCustomerEntity getCustomer() {
        return customer;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public JpaOrderAddressEntity getAddress() {
        return address;
    }

    public List<JpaOrderItemEntity> getItems() {
        return items;
    }

    public void setItems(List<JpaOrderItemEntity> items) {
        this.items = items;
    }

    public int getVersion() {
        return version;
    }
}
