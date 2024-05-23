package pl.schabik.order.infrastructure.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record GetOrderItemResponse(
        UUID productId,
        Integer quantity,
        BigDecimal price,
        BigDecimal totalPrice
) {
}