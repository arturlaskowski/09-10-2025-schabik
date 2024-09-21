package pl.schabik.order.application.replication;

import java.util.UUID;

interface CustomerProjectionRepository {

    CustomerProjection save(CustomerProjection customerProjection);

    boolean existsById(UUID id);
}