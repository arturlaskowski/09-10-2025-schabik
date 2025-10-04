package pl.schabik.infrastructure.persistence;

public record JpaOrderItemId(
        Integer id,
        JpaOrderEntity order) {
}
