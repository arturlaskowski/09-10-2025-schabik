package pl.schabik.usecase;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import pl.schabik.domain.Customer;
import pl.schabik.domain.OrderId;
import pl.schabik.domain.OrderStatus;
import pl.schabik.usecase.common.OrderNotFoundException;
import pl.schabik.usecase.createorder.CreateOrderAddressDto;
import pl.schabik.usecase.createorder.CreateOrderDto;
import pl.schabik.usecase.createorder.CreateOrderItemDto;
import pl.schabik.usecase.createorder.CreateOrderService;
import pl.schabik.usecase.payorder.PayOrderService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class PayOrderServiceTest {

    InMemoryOrderRepository orderRepository = new InMemoryOrderRepository();
    InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository();
    PayOrderService payOrderService = new PayOrderService(orderRepository);
    CreateOrderService createOrderService = new CreateOrderService(orderRepository, customerRepository);

    @AfterEach
    void cleanUp() {
        orderRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void shouldPaidOrder() {
        // given
        var customerId = customerRepository.save(new Customer("Arnold", "Boczek", "boczek@gmail.com")).getId();
        var createOrderDto = getCreateOrderDto(customerId);
        var orderId = createOrderService.createOrder(createOrderDto);

        // when
        payOrderService.pay(orderId);

        // then
        var paidOrder = orderRepository.findById(orderId).orElseThrow();
        assertThat(paidOrder.getStatus()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    void shouldThrowExceptionWhenOrderDoesNotExistForPayment() {
        // given
        var nonExistentOrderId = new OrderId(UUID.randomUUID());

        // expected
        assertThatThrownBy(() -> payOrderService.pay(nonExistentOrderId))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessage(OrderNotFoundException.createExceptionMessage(nonExistentOrderId));
    }

    private CreateOrderDto getCreateOrderDto(UUID customerId) {
        var items = List.of(new CreateOrderItemDto(UUID.randomUUID(), 2, new BigDecimal("10.00"), new BigDecimal("20.00")),
                new CreateOrderItemDto(UUID.randomUUID(), 1, new BigDecimal("34.56"), new BigDecimal("34.56")));
        var address = new CreateOrderAddressDto("Małysza", "94-000", "Adasiowo", "12");
        return new CreateOrderDto(customerId, new BigDecimal("54.56"), items, address);
    }
}