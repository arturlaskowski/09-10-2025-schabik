package pl.schabik.order.domain;

import pl.schabik.order.domain.vo.OrderId;

import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(OrderId id);
}