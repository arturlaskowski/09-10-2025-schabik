package pl.schabik.repository;

import pl.schabik.model.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {

    Customer save(Customer customer);

    Optional<Customer> findById(UUID id);

    boolean existsByEmail(String email);
}
