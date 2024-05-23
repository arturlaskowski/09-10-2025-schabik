package pl.schabik.order.application.replication;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
class SqlCustomerProjectionRepository implements CustomerProjectionRepository {

    private final CustomerProjectionRepositoryJpa customerProjectionRepositoryJpa;

    public SqlCustomerProjectionRepository(CustomerProjectionRepositoryJpa customerProjectionRepositoryJpa) {
        this.customerProjectionRepositoryJpa = customerProjectionRepositoryJpa;
    }

    @Override
    public void save(CustomerProjection customerProjection) {
        customerProjectionRepositoryJpa.save(customerProjection);
    }

    @Override
    public boolean existsById(UUID id) {
        return customerProjectionRepositoryJpa.existsById(id);
    }
}

@Repository
interface CustomerProjectionRepositoryJpa extends CrudRepository<CustomerProjection, UUID> {
}