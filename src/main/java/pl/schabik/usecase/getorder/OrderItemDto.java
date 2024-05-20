package pl.schabik.usecase.getorder;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemDto(
        UUID productId,
        Integer quantity,
        BigDecimal price,
        BigDecimal totalPrice
) {
}
