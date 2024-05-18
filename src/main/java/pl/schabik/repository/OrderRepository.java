package pl.schabik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.schabik.model.Order;
import pl.schabik.model.OrderId;

public interface OrderRepository extends JpaRepository<Order, OrderId> {
}
