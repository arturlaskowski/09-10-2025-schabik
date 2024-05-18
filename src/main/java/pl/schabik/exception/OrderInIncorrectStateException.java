package pl.schabik.exception;

import pl.schabik.model.OrderStatus;

import java.util.UUID;

public class OrderInIncorrectStateException extends RuntimeException {

    public OrderInIncorrectStateException(UUID orderId, OrderStatus status) {
        super("Order is in incorrect state. OrderId: " + orderId + " status: " + status.name());
    }
}
