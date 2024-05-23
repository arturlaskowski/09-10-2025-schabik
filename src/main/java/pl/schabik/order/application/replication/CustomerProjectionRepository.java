package pl.schabik.order.application.replication;

import java.util.UUID;

public interface CustomerProjectionRepository {

    void save(CustomerProjection customerProjection);

    boolean existsById(UUID id);
}