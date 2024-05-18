package pl.schabik.repository;

import pl.schabik.model.Order;
import pl.schabik.model.OrderId;

import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(OrderId id);
}
