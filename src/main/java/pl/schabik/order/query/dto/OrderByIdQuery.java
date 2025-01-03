package pl.schabik.order.query.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderByIdQuery(
        UUID id,
        UUID customerId,
        BigDecimal price,
        String status,
        List<OrderItemDto> items,
        OrderAddressDto address) {
}