package pl.schabik.order.application;

import pl.schabik.order.domain.Order;
import pl.schabik.order.domain.vo.OrderId;
import pl.schabik.order.domain.OrderRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class InMemoryOrderRepository implements OrderRepository {

    private final Map<OrderId, Order> store = new HashMap<>();

    @Override
    public Order save(Order order) {
        store.put(order.getId(), order);
        return order;
    }

    @Override
    public Optional<Order> findById(OrderId id) {
        return Optional.ofNullable(store.get(id));
    }

    void deleteAll() {
        store.clear();
    }
}