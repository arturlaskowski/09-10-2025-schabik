package pl.schabik.order.command.approve;

import jakarta.validation.constraints.NotNull;
import pl.schabik.common.command.Command;
import pl.schabik.order.domain.vo.OrderId;

public record ApproveOrderCommand(
        @NotNull OrderId orderId) implements Command {
}
