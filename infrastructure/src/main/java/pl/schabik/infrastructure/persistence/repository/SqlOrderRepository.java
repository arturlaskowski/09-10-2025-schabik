package pl.schabik.infrastructure.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.schabik.domain.Order;
import pl.schabik.domain.OrderId;
import pl.schabik.domain.OrderRepository;
import pl.schabik.infrastructure.persistence.DomainToPersistenceMapper;
import pl.schabik.infrastructure.persistence.JpaOrderEntity;
import pl.schabik.infrastructure.persistence.PersistenceToDomainMapper;

import java.util.Optional;
import java.util.UUID;

@Repository
public class SqlOrderRepository implements OrderRepository {

    private final OrderRepositoryJpa orderRepositoryJpa;

    public SqlOrderRepository(OrderRepositoryJpa orderRepositoryJpa) {
        this.orderRepositoryJpa = orderRepositoryJpa;
    }

    @Override
    public Order save(Order order) {
        JpaOrderEntity entity = DomainToPersistenceMapper.toJpaEntity(order);
        JpaOrderEntity savedEntity = orderRepositoryJpa.save(entity);
        return PersistenceToDomainMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Order> findById(OrderId orderId) {
        return orderRepositoryJpa.findById(orderId.id())
                .map(PersistenceToDomainMapper::toDomain);
    }
}

@Repository
interface OrderRepositoryJpa extends CrudRepository<JpaOrderEntity, UUID> {
}
