package pl.schabik.exception;

import pl.schabik.model.OrderId;

public class OrderNotFoundException extends RuntimeException {

    public static String createExceptionMessage(OrderId orderId) {
        return String.format("Could not find order with orderId:  %s", orderId.id());
    }

    public OrderNotFoundException(OrderId orderId) {
        super(createExceptionMessage(orderId));
    }
}
