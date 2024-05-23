package pl.schabik.order.infrastructure.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record GetOrderResponse(
        UUID id,
        UUID customerId,
        BigDecimal price,
        String status,
        List<GetOrderItemResponse> items,
        GetOrderAddressResponse address) {
}