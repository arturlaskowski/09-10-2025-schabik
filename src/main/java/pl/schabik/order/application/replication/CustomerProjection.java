package pl.schabik.order.application.replication;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
class CustomerProjection {

    @Id
    private UUID id;

    public CustomerProjection(UUID id) {
        this.id = id;
    }

    protected CustomerProjection() {
    }

    public UUID getId() {
        return id;
    }
}