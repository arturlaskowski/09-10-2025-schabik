package pl.schabik.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;


@IdClass(OrderItemId.class)
@Table(name = "order_items")
@Entity
public class OrderItem {

    @Id
    private Integer id;

    @Id
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private UUID productId;

    @NotNull
    @Min(0)
    private BigDecimal price;

    @NotNull
    @Min(0)
    private Integer quantity;

    @NotNull
    @Min(0)
    private BigDecimal totalPrice;

    //For JPA
    protected OrderItem() {
    }

    public OrderItem(UUID productId, BigDecimal price, Integer quantity, BigDecimal totalPrice) {
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        validateQuantity();
        validatePrice();
    }

    private void validateQuantity() {
        if (quantity < 0) {
            throw new OrderDomainException("Quantity must be non-negative but was: " + quantity);
        }
    }

    private void validatePrice() {
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new OrderDomainException("Price must be greater than zero but was: " + price);
        }
        if (!price.multiply(BigDecimal.valueOf(quantity)).equals(totalPrice)) {
            throw new OrderDomainException("Total price should be equal to price multiplied by quantity. Expected: " +
                    price.multiply(BigDecimal.valueOf(quantity)) + " but was: " + totalPrice);
        }
    }

    void initializeBasketItem(Order order, Integer itemNumber) {
        this.order = order;
        this.id = itemNumber;
    }

    public Integer getId() {
        return id;
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
