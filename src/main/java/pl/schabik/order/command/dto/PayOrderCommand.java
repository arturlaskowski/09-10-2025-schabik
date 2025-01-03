package pl.schabik.order.command.dto;

import jakarta.validation.constraints.NotNull;
import pl.schabik.order.domain.vo.OrderId;

public record PayOrderCommand(
        @NotNull OrderId orderId) {
}
