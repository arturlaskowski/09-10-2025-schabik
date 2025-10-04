package pl.schabik.domain;

import java.util.UUID;

public class OrderItem {

    private Integer id;
    private OrderId orderId;
    private final UUID productId;
    private final Money price;
    private final Quantity quantity;
    private final Money totalPrice;

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

    public void initializeBasketItem(Order order, Integer itemNumber) {
        this.orderId = order.getId();
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