package pl.schabik.order.command;

import pl.schabik.common.CustomerCreatedEvent;
import pl.schabik.order.command.create.CreateOrderAddressDto;
import pl.schabik.order.command.create.CreateOrderCommand;
import pl.schabik.order.command.create.CreateOrderHandler;
import pl.schabik.order.command.create.CreateOrderItemDto;
import pl.schabik.order.domain.vo.OrderId;
import pl.schabik.order.replication.CustomerProjectionService;
import pl.schabik.order.replication.InMemoryCustomerProjectionRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class OrderTestUtil {

    public static InMemoryOrderRepository orderRepository = new InMemoryOrderRepository();
    public static InMemoryCustomerProjectionRepository customerRepository = new InMemoryCustomerProjectionRepository();
    public static CustomerProjectionService customerService = new CustomerProjectionService(customerRepository);
    public static CreateOrderHandler createOrderHandler = new CreateOrderHandler(orderRepository, customerService);

    private OrderTestUtil() {
    }

    public static CreateOrderCommand getCreateOrderCommand(OrderId orderId, UUID customerId) {
        var items = List.of(new CreateOrderItemDto(UUID.randomUUID(), 2, new BigDecimal("10.00"), new BigDecimal("20.00")),
                new CreateOrderItemDto(UUID.randomUUID(), 1, new BigDecimal("34.56"), new BigDecimal("34.56")));
        var address = new CreateOrderAddressDto("Małysza", "94-000", "Adasiowo", "12");
        return new CreateOrderCommand(orderId, customerId, new BigDecimal("54.56"), items, address);
    }

    public static OrderId createCustomerAndOrder() {
        var orderId = new OrderId(UUID.randomUUID());
        var customerId = UUID.randomUUID();
        customerService.replicateCustomer(new CustomerCreatedEvent(customerId));
        var createOrderCommand = getCreateOrderCommand(orderId, customerId);
        createOrderHandler.handle(createOrderCommand);
        return orderId;
    }

    public static InMemoryOrderRepository orderRepository() {
        return orderRepository;
    }

    public static InMemoryCustomerProjectionRepository customerRepository() {
        return customerRepository;
    }

    public static CustomerProjectionService customerService() {
        return customerService;
    }

    public static CreateOrderHandler createOrderHandler() {
        return createOrderHandler;
    }
}
