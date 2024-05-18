package pl.schabik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.schabik.model.Order;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
