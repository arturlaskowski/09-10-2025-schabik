package pl.schabik.domain;

import java.util.UUID;

public record OrderId(UUID orderId) {

    public static OrderId newOne() {
        return new OrderId(UUID.randomUUID());
    }

    public UUID id() {
        return orderId;
    }
}

