package pl.schabik.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.schabik.domain.Order;
import pl.schabik.domain.OrderId;
import pl.schabik.domain.OrderRepository;

import java.util.Optional;

@Repository
public class SqlOrderRepository implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepository;

    public SqlOrderRepository(JpaOrderRepository jpaOrderRepository) {
        this.jpaOrderRepository = jpaOrderRepository;
    }

    @Override
    public Order save(Order order) {
        return jpaOrderRepository.save(order);
    }

    @Override
    public Optional<Order> findById(OrderId id) {
        return jpaOrderRepository.findById(id);
    }
}

@Repository
interface JpaOrderRepository extends JpaRepository<Order, OrderId> {
}