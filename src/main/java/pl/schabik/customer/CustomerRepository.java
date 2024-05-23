package pl.schabik.customer;

import java.util.Optional;
import java.util.UUID;

interface CustomerRepository {

    Customer save(Customer customer);

    Optional<Customer> findById(UUID id);

    boolean existsByEmail(String email);

    boolean existsById(UUID id);
}