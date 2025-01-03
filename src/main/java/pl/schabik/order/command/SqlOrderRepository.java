package pl.schabik.order.command;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.schabik.order.domain.Order;
import pl.schabik.order.domain.OrderRepository;
import pl.schabik.order.domain.vo.OrderId;

import java.util.Optional;

@Repository
class SqlOrderRepository implements OrderRepository {

    private final OrderRepositoryJpa orderRepositoryJpa;

    public SqlOrderRepository(OrderRepositoryJpa orderRepositoryJpa) {
        this.orderRepositoryJpa = orderRepositoryJpa;
    }

    @Override
    public Order save(Order order) {
        return orderRepositoryJpa.save(order);
    }

    @Override
    public Optional<Order> findById(OrderId id) {
        return orderRepositoryJpa.findById(id);
    }
}

@Repository
interface OrderRepositoryJpa extends CrudRepository<Order, OrderId> {
}
