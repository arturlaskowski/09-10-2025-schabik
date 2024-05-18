package pl.schabik.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.schabik.domain.Customer;
import pl.schabik.domain.CustomerRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class SqlCustomerRepository implements CustomerRepository {

    private final JpaCustomerRepository jpaCustomerRepository;

    public SqlCustomerRepository(JpaCustomerRepository jpaCustomerRepository) {
        this.jpaCustomerRepository = jpaCustomerRepository;
    }

    @Override
    public Customer save(Customer customer) {
        return jpaCustomerRepository.save(customer);
    }

    @Override
    public Optional<Customer> findById(UUID id) {
        return jpaCustomerRepository.findById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaCustomerRepository.existsByEmail(email);
    }
}

@Repository
interface JpaCustomerRepository extends JpaRepository<Customer, UUID> {
    boolean existsByEmail(String email);
}