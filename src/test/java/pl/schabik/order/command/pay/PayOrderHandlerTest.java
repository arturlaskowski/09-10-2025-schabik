package pl.schabik.order.command.pay;

import org.junit.jupiter.api.Test;
import pl.schabik.order.command.exception.OrderNotFoundException;
import pl.schabik.order.domain.OrderStatus;
import pl.schabik.order.domain.vo.OrderId;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static pl.schabik.order.command.OrderTestUtil.createCustomerAndOrder;
import static pl.schabik.order.command.OrderTestUtil.orderRepository;

class PayOrderHandlerTest {

    private final PayOrderHandler payOrderHandler = new PayOrderHandler(orderRepository);

    @Test
    void shouldPaidOrder() {
        // given
        var orderId = createCustomerAndOrder();

        // when
        payOrderHandler.handle(new PayOrderCommand(orderId));

        // then
        var paidOrder = orderRepository.findById(orderId).orElseThrow();
        assertThat(paidOrder.getStatus()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    void shouldThrowExceptionWhenOrderDoesNotExistForPayment() {
        // given
        var nonExistentOrderId = new OrderId(UUID.randomUUID());

        // expected
        assertThatThrownBy(() -> payOrderHandler.handle(new PayOrderCommand(nonExistentOrderId)))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessage(OrderNotFoundException.createExceptionMessage(nonExistentOrderId));
    }

}