package pl.schabik.order.query.dto;


import pl.schabik.order.domain.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderPageQuery(UUID orderId, Instant createAt, OrderStatus status, BigDecimal price) {
}
