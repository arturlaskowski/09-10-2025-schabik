package pl.schabik.customer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
class SqlCustomerRepository implements CustomerRepository {

    private final CustomerRepositoryJpa customerRepositoryJpa;

    public SqlCustomerRepository(CustomerRepositoryJpa customerRepositoryJpa) {
        this.customerRepositoryJpa = customerRepositoryJpa;
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepositoryJpa.save(customer);
    }

    @Override
    public Optional<Customer> findById(UUID id) {
        return customerRepositoryJpa.findById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return customerRepositoryJpa.existsByEmail(email);
    }

    @Override
    public boolean existsById(UUID id) {
        return customerRepositoryJpa.existsById(id);
    }
}

@Repository
interface CustomerRepositoryJpa extends CrudRepository<Customer, UUID> {

    boolean existsByEmail(String email);
}
