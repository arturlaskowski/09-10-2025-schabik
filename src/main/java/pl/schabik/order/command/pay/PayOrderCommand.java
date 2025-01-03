package pl.schabik.order.command.pay;

import jakarta.validation.constraints.NotNull;
import pl.schabik.common.command.Command;
import pl.schabik.order.domain.vo.OrderId;

public record PayOrderCommand(
        @NotNull OrderId orderId) implements Command {
}
