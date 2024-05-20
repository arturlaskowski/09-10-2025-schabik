package pl.schabik.usecase;

import pl.schabik.domain.Customer;
import pl.schabik.domain.CustomerRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

class InMemoryCustomerRepository implements CustomerRepository {

    private final Map<UUID, Customer> store = new HashMap<>();

    @Override
    public Customer save(Customer customer) {
        store.put(customer.getId(), customer);
        return customer;
    }

    @Override
    public Optional<Customer> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public boolean existsByEmail(String email) {
        return store.values().stream()
                .anyMatch(c -> c.getEmail().equals(email));
    }

    public void deleteAll() {
        store.clear();
    }
}