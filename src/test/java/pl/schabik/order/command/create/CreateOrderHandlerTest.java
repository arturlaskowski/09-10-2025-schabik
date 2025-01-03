package pl.schabik.order.command.create;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import pl.schabik.common.CustomerCreatedEvent;
import pl.schabik.order.domain.Order;
import pl.schabik.order.domain.OrderStatus;
import pl.schabik.order.domain.vo.Money;
import pl.schabik.order.domain.vo.OrderId;
import pl.schabik.order.domain.vo.Quantity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static pl.schabik.order.command.OrderTestUtil.*;

class CreateOrderHandlerTest {

    @AfterEach
    void cleanUp() {
        orderRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void shouldCreateOrder() {
        // given
        var customerId = UUID.randomUUID();
        var orderId = OrderId.newOne();
        customerService.replicateCustomer(new CustomerCreatedEvent(customerId));
        var createOrderCommand = getCreateOrderCommand(orderId, customerId);

        // when
        createOrderHandler.handle(createOrderCommand);

        // then
        var savedOrder = orderRepository.findById(orderId).orElseThrow();
        assertThat(savedOrder)
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("customerId", createOrderCommand.customerId())
                .hasFieldOrPropertyWithValue("price", new Money(createOrderCommand.price()))
                .hasFieldOrPropertyWithValue("status", OrderStatus.PENDING)
                .extracting(Order::getAddress)
                .hasFieldOrPropertyWithValue("street", createOrderCommand.address().street())
                .hasFieldOrPropertyWithValue("postalCode", createOrderCommand.address().postalCode())
                .hasFieldOrPropertyWithValue("city", createOrderCommand.address().city())
                .hasFieldOrPropertyWithValue("houseNo", createOrderCommand.address().houseNo());

        assertThat(savedOrder.getItems()).hasSize(createOrderCommand.items().size())
                .zipSatisfy(createOrderCommand.items(), (orderItem, orderItemDto) -> {
                    assertThat(orderItem.getProductId()).isEqualTo(orderItemDto.productId());
                    assertThat(orderItem.getPrice()).isEqualTo(new Money(orderItemDto.price()));
                    assertThat(orderItem.getQuantity()).isEqualTo(new Quantity(orderItemDto.quantity()));
                    assertThat(orderItem.getTotalPrice()).isEqualTo(new Money(orderItemDto.totalPrice()));
                });
    }

    @Test
    void shouldThrowExceptionWhenCustomerDoesNotExistWhileCreatingOrder() {
        // given
        var orderId = OrderId.newOne();
        var nonExistentCustomerId = UUID.randomUUID();
        var createOrderCommand = getCreateOrderCommand(orderId, nonExistentCustomerId);

        // expected
        assertThatThrownBy(() -> createOrderHandler.handle(createOrderCommand))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessage(CustomerNotFoundException.createExceptionMessage(nonExistentCustomerId));
    }
}