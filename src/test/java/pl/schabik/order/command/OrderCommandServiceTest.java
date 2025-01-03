package pl.schabik.order.command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import pl.schabik.common.CustomerCreatedEvent;
import pl.schabik.order.command.exception.CustomerNotFoundException;
import pl.schabik.order.command.exception.OrderNotFoundException;
import pl.schabik.order.replication.CustomerProjectionService;
import pl.schabik.order.replication.InMemoryCustomerProjectionRepository;
import pl.schabik.order.command.dto.*;
import pl.schabik.order.domain.Order;
import pl.schabik.order.domain.OrderStatus;
import pl.schabik.order.domain.vo.Money;
import pl.schabik.order.domain.vo.OrderId;
import pl.schabik.order.domain.vo.Quantity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class OrderCommandServiceTest {

    InMemoryOrderRepository orderRepository = new InMemoryOrderRepository();
    InMemoryCustomerProjectionRepository customerRepository = new InMemoryCustomerProjectionRepository();
    CustomerProjectionService customerService = new CustomerProjectionService(customerRepository);
    OrderCommandService orderCommandService = new OrderCommandService(orderRepository, customerService);

    @AfterEach
    void cleanUp() {
        orderRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void shouldCreateOrder() {
        // given
        var customerId = UUID.randomUUID();
        customerService.replicateCustomer(new CustomerCreatedEvent(customerId));
        var createOrderDto = getCreateOrderCommand(customerId);

        // when
        var orderId = orderCommandService.createOrder(createOrderDto);

        // then
        var savedOrder = orderRepository.findById(orderId).orElseThrow();
        assertThat(savedOrder)
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("customerId", createOrderDto.customerId())
                .hasFieldOrPropertyWithValue("price", new Money(createOrderDto.price()))
                .hasFieldOrPropertyWithValue("status", OrderStatus.PENDING)
                .extracting(Order::getAddress)
                .hasFieldOrPropertyWithValue("street", createOrderDto.address().street())
                .hasFieldOrPropertyWithValue("postalCode", createOrderDto.address().postalCode())
                .hasFieldOrPropertyWithValue("city", createOrderDto.address().city())
                .hasFieldOrPropertyWithValue("houseNo", createOrderDto.address().houseNo());

        assertThat(savedOrder.getItems()).hasSize(createOrderDto.items().size())
                .zipSatisfy(createOrderDto.items(), (orderItem, orderItemDto) -> {
                    assertThat(orderItem.getProductId()).isEqualTo(orderItemDto.productId());
                    assertThat(orderItem.getPrice()).isEqualTo(new Money(orderItemDto.price()));
                    assertThat(orderItem.getQuantity()).isEqualTo(new Quantity(orderItemDto.quantity()));
                    assertThat(orderItem.getTotalPrice()).isEqualTo(new Money(orderItemDto.totalPrice()));
                });
    }

    @Test
    void shouldThrowExceptionWhenCustomerDoesNotExistWhileCreatingOrder() {
        // given
        var nonExistentCustomerId = UUID.randomUUID();
        var createOrderDto = getCreateOrderCommand(nonExistentCustomerId);

        // expected
        assertThatThrownBy(() -> orderCommandService.createOrder(createOrderDto))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessage(CustomerNotFoundException.createExceptionMessage(nonExistentCustomerId));
    }

    @Test
    void shouldPaidOrder() {
        // given
        var customerId = UUID.randomUUID();
        customerService.replicateCustomer(new CustomerCreatedEvent(customerId));
        var createOrderDto = getCreateOrderCommand(customerId);
        var orderId = orderCommandService.createOrder(createOrderDto);

        // when
        orderCommandService.pay(new PayOrderCommand(orderId));

        // then
        var paidOrder = orderRepository.findById(orderId).orElseThrow();
        assertThat(paidOrder.getStatus()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    void shouldThrowExceptionWhenOrderDoesNotExistForPayment() {
        // given
        var nonExistentOrderId = new OrderId(UUID.randomUUID());

        // expected
        assertThatThrownBy(() -> orderCommandService.pay(new PayOrderCommand(nonExistentOrderId)))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessage(OrderNotFoundException.createExceptionMessage(nonExistentOrderId));
    }

    @Test
    void shouldApproveOrder() {
        // given
        var customerId = UUID.randomUUID();
        customerService.replicateCustomer(new CustomerCreatedEvent(customerId));
        var createOrderCommand = getCreateOrderCommand(customerId);
        var orderId = orderCommandService.createOrder(createOrderCommand);
        orderCommandService.pay(new PayOrderCommand(orderId));
        var approveOrderCommand = new ApproveOrderCommand(orderId);

        // when
        orderCommandService.approve(approveOrderCommand);

        // then
        var order = orderRepository.findById(orderId).orElseThrow();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.APPROVED);
    }

    @Test
    void shouldThrowExceptionWhenOrderDoesNotExistForApprove() {
        // given
        var nonExistentOrderId = OrderId.newOne();
        var approveOrderCommand = new ApproveOrderCommand(nonExistentOrderId);

        // expected
        assertThatThrownBy(() -> orderCommandService.approve(approveOrderCommand))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessage(OrderNotFoundException.createExceptionMessage(nonExistentOrderId));
    }

    private CreateOrderCommand getCreateOrderCommand(UUID customerId) {
        var items = List.of(new CreateOrderItemDto(UUID.randomUUID(), 2, new BigDecimal("10.00"), new BigDecimal("20.00")),
                new CreateOrderItemDto(UUID.randomUUID(), 1, new BigDecimal("34.56"), new BigDecimal("34.56")));
        var address = new CreateOrderAddressDto("Małysza", "94-000", "Adasiowo", "12");
        return new CreateOrderCommand(customerId, new BigDecimal("54.56"), items, address);
    }
}