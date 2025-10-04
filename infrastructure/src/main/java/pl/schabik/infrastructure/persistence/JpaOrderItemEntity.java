package pl.schabik.infrastructure.persistence;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

@IdClass(JpaOrderItemId.class)
@Table(name = "order_items")
@Entity
public class JpaOrderItemEntity {

    @Id
    private Integer id;

    @Id
    @ManyToOne
    @JoinColumn(name = "order_id")
    private JpaOrderEntity order;

    private UUID productId;

    @NotNull
    @Column(name = "price")
    private BigDecimal price;

    @NotNull
    @Column(name = "quantity")
    private Integer quantity;

    @NotNull
    @Column(name = "totalPrice")
    private BigDecimal totalPrice;

    protected JpaOrderItemEntity() {
    }

    public JpaOrderItemEntity(Integer id, JpaOrderEntity order, UUID productId, BigDecimal price, Integer quantity, BigDecimal totalPrice) {
        this.id = id;
        this.order = order;
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public Integer getId() {
        return id;
    }

    public JpaOrderEntity getOrder() {
        return order;
    }

    public UUID getProductId() {
        return productId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
}
