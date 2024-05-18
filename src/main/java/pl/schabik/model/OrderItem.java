package pl.schabik.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

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
    @AttributeOverride(name = "amount", column = @Column(name = "price"))
    private Money price;

    @NotNull
    @AttributeOverride(name = "value", column = @Column(name = "quantity"))
    private Quantity quantity;

    @NotNull
    @AttributeOverride(name = "amount", column = @Column(name = "totalPrice"))
    private Money totalPrice;

    //For JPA
    protected OrderItem() {
    }

    public OrderItem(UUID productId, Money price, Quantity quantity, Money totalPrice) {
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        validatePrice();
    }

    private void validatePrice() {
        if (!price.multiply(quantity.value()).equals(totalPrice)) {
            throw new OrderDomainException("Total price should be equal to price multiplied by quantity. Expected: " +
                    price.multiply(quantity.value()) + " but was: " + totalPrice);
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

    public Money getPrice() {
        return price;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Money getTotalPrice() {
        return totalPrice;
    }
}