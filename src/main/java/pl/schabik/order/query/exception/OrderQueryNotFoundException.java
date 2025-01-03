package pl.schabik.order.query.exception;

import pl.schabik.order.domain.vo.OrderId;

public class OrderQueryNotFoundException extends RuntimeException {

    public static String createExceptionMessage(OrderId orderId) {
        return String.format("Could not find order with orderId:  %s", orderId.id());
    }

    public OrderQueryNotFoundException(OrderId orderId) {
        super(createExceptionMessage(orderId));
    }
}
