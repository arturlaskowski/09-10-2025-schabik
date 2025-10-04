package pl.schabik.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.schabik.domain.Customer;
import pl.schabik.domain.CustomerRepository;
import pl.schabik.infrastructure.persistence.JpaCustomerEntity;
import pl.schabik.infrastructure.persistence.DomainToPersistenceMapper;
import pl.schabik.infrastructure.persistence.PersistenceToDomainMapper;

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
        JpaCustomerEntity entity = DomainToPersistenceMapper.toJpaEntity(customer);
        JpaCustomerEntity savedEntity = customerRepositoryJpa.save(entity);
        return PersistenceToDomainMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Customer> findById(UUID id) {
        return customerRepositoryJpa.findById(id)
                .map(PersistenceToDomainMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return customerRepositoryJpa.existsByEmail(email);
    }
}

@Repository
interface CustomerRepositoryJpa extends JpaRepository<JpaCustomerEntity, UUID> {
    boolean existsByEmail(String email);
}
