package pl.schabik.order.command.create;

import org.springframework.stereotype.Service;
import pl.schabik.common.command.CommandHandler;
import pl.schabik.order.domain.Order;
import pl.schabik.order.domain.OrderAddress;
import pl.schabik.order.domain.OrderItem;
import pl.schabik.order.domain.OrderRepository;
import pl.schabik.order.domain.vo.Money;
import pl.schabik.order.domain.vo.Quantity;
import pl.schabik.order.replication.CustomerProjectionService;

import java.util.List;
import java.util.UUID;

@Service
public class CreateOrderHandler implements CommandHandler<CreateOrderCommand> {

    private final OrderRepository orderRepository;
    private final CustomerProjectionService customerProjectionService;

    public CreateOrderHandler(OrderRepository orderRepository, CustomerProjectionService customerProjectionService) {
        this.orderRepository = orderRepository;
        this.customerProjectionService = customerProjectionService;
    }

    public void handle(CreateOrderCommand createOrderCommand) {
        validateCustomerExists(createOrderCommand.customerId());
        var items = convertToCreateOrderItems(createOrderCommand.items());
        var orderAddress = convertToCreateOrderAddress(createOrderCommand.address());

        var order = new Order(createOrderCommand.orderId(), createOrderCommand.customerId(), new Money(createOrderCommand.price()),
                items, orderAddress);

        orderRepository.save(order);
    }

    private void validateCustomerExists(UUID customerId) {
        if (!customerProjectionService.existsById(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }
    }

    private List<OrderItem> convertToCreateOrderItems(List<CreateOrderItemDto> itemDtos) {
        return itemDtos.stream()
                .map(itemDto -> new OrderItem(
                        itemDto.productId(),
                        new Money(itemDto.price()),
                        new Quantity(itemDto.quantity()),
                        new Money(itemDto.totalPrice())
                )).toList();
    }

    private OrderAddress convertToCreateOrderAddress(CreateOrderAddressDto addressDto) {
        if (addressDto != null) {
            return new OrderAddress(
                    addressDto.street(),
                    addressDto.postalCode(),
                    addressDto.city(),
                    addressDto.houseNo()
            );
        }
        return null;
    }
}
