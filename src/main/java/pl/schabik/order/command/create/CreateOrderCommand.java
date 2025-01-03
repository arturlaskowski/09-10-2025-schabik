package pl.schabik.order.command.create;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import pl.schabik.common.command.Command;
import pl.schabik.order.domain.vo.OrderId;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateOrderCommand(
        @NotNull OrderId orderId,
        @NotNull UUID customerId,
        @NotNull @Min(0) BigDecimal price,
        @Valid @NotNull List<CreateOrderItemDto> items,
        @Valid CreateOrderAddressDto address
) implements Command {
}

