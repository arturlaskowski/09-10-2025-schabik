package pl.schabik.infrastructure.dto;

import java.time.Instant;

public record ErrorResponse(
        String message,
        Instant timestamp) {

    public ErrorResponse(String message) {
        this(message, Instant.now());
    }
}
