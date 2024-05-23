package pl.schabik.order.application.replication;

import java.util.UUID;

interface CustomerProjectionRepository {

    void save(CustomerProjection customerProjection);

    boolean existsById(UUID id);
}