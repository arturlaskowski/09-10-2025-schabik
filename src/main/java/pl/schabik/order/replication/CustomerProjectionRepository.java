package pl.schabik.order.replication;

import java.util.UUID;

interface CustomerProjectionRepository {

    CustomerProjection save(CustomerProjection customerProjection);

    boolean existsById(UUID id);
}