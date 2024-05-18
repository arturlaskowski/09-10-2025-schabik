package pl.schabik.domain;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class OrderId {

    public static OrderId newOne() {
        return new OrderId(UUID.randomUUID());
    }

    private UUID orderId;

    public OrderId(UUID uuid) {
        this.orderId = uuid;
    }

    protected OrderId() {
    }

    public UUID id() {
        return orderId;
    }

}
