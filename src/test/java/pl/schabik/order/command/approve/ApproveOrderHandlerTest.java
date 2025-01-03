package pl.schabik.order.command.approve;


import org.junit.jupiter.api.Test;
import pl.schabik.order.command.exception.OrderNotFoundException;
import pl.schabik.order.command.pay.PayOrderCommand;
import pl.schabik.order.command.pay.PayOrderHandler;
import pl.schabik.order.domain.vo.OrderId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static pl.schabik.order.command.OrderTestUtil.createCustomerAndOrder;
import static pl.schabik.order.command.OrderTestUtil.orderRepository;

class ApproveOrderHandlerTest {

    private final PayOrderHandler payOrderHandler = new PayOrderHandler(orderRepository);
    private final ApproveOrderHandler approveOrderHandler = new ApproveOrderHandler(orderRepository);

    @Test
    void shouldApproveOrder() {
        // given
        var orderId = createCustomerAndOrder();
        payOrderHandler.handle(new PayOrderCommand(orderId));
        var approveOrderCommand = new ApproveOrderCommand(orderId);

        // when
        approveOrderHandler.handle(approveOrderCommand);

        // then
        var order = orderRepository.findById(orderId).orElseThrow();
        assertThat(order.isApproved()).isTrue();
    }

    @Test
    void shouldThrowExceptionWhenOrderDoesNotExistForApprove() {
        // given
        var nonExistentOrderId = OrderId.newOne();
        var approveOrderCommand = new ApproveOrderCommand(nonExistentOrderId);

        // expected
        assertThatThrownBy(() -> approveOrderHandler.handle(approveOrderCommand))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessage(OrderNotFoundException.createExceptionMessage(nonExistentOrderId));
    }
}