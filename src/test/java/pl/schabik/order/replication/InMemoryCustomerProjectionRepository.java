package pl.schabik.order.replication;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InMemoryCustomerProjectionRepository implements CustomerProjectionRepository {

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

    public void deleteAll() {
        store.clear();
    }
}