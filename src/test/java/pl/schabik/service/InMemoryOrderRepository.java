package pl.schabik.service;

import pl.schabik.model.Order;
import pl.schabik.model.OrderId;
import pl.schabik.repository.OrderRepository;

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

    public void deleteAll() {
        store.clear();
    }
}