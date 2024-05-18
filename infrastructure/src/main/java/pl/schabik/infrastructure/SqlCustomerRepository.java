package pl.schabik.infrastructure;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.schabik.domain.Customer;
import pl.schabik.domain.CustomerRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class SqlCustomerRepository implements CustomerRepository {

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
}

@Repository
interface CustomerRepositoryJpa extends CrudRepository<Customer, UUID> {

    boolean existsByEmail(String email);
}
