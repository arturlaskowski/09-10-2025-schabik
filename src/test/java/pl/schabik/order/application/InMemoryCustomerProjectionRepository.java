package pl.schabik.order.application;


import pl.schabik.order.application.replication.CustomerProjection;
import pl.schabik.order.application.replication.CustomerProjectionRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class InMemoryCustomerProjectionRepository implements CustomerProjectionRepository {

    private final Map<UUID, CustomerProjection> store = new HashMap<>();

    @Override
    public CustomerProjection save(CustomerProjection customer) {
        store.put(customer.getId(), customer);
        return customer;
    }

    @Override
    public boolean existsById(UUID id) {
        return store.containsKey(id);
    }

    void deleteAll() {
        store.clear();
    }
}