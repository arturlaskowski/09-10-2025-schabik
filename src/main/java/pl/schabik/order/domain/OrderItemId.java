package pl.schabik.order.domain;

public record OrderItemId(
        Integer id,
        Order order) {
}