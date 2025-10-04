package pl.schabik.infrastructure.persistence;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public record JpaOrderId(UUID orderId) {
}
