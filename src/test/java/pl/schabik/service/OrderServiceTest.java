package pl.schabik.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.schabik.controller.CreateOrderDto;
import pl.schabik.controller.OrderAddressDto;
import pl.schabik.controller.OrderItemDto;
import pl.schabik.exception.CustomerNotFoundException;
import pl.schabik.exception.OrderNotFoundException;
import pl.schabik.model.*;
import pl.schabik.repository.CustomerRepository;
import pl.schabik.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @AfterEach
    void cleanUp() {
        orderRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void shouldCreateOrder() {
        // given
        var customer = new Customer();
        customer.setId(UUID.randomUUID());
        customer.setFirstName("Ferdek");
        customer.setLastName("Kiepski");
        customer.setEmail("ferdzio@gmail.com");
        var customerId = customerRepository.save(customer).getId();
        var createOrderDto = getCreateOrderDto(customerId);

        // when
        var orderId = orderService.createOrder(createOrderDto);

        // then
        var savedOrder = orderRepository.findById(orderId).orElseThrow();
        assertThat(savedOrder)
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("customer.id", createOrderDto.customerId())
                .hasFieldOrPropertyWithValue("price", createOrderDto.price())
                .hasFieldOrPropertyWithValue("status", OrderStatus.PENDING)
                .extracting(Order::getAddress)
                .hasFieldOrPropertyWithValue("street", createOrderDto.address().street())
                .hasFieldOrPropertyWithValue("postalCode", createOrderDto.address().postalCode())
                .hasFieldOrPropertyWithValue("city", createOrderDto.address().city())
                .hasFieldOrPropertyWithValue("houseNo", createOrderDto.address().houseNo());

        assertThat(savedOrder.getItems()).hasSize(createOrderDto.items().size())
                .zipSatisfy(createOrderDto.items(), (orderItem, orderItemDto) -> {
                    assertThat(orderItem.getProductId()).isEqualTo(orderItemDto.productId());
                    assertThat(orderItem.getPrice()).isEqualTo(orderItemDto.price());
                    assertThat(orderItem.getQuantity()).isEqualTo(orderItemDto.quantity());
                    assertThat(orderItem.getTotalPrice()).isEqualTo(orderItemDto.totalPrice());
                });
    }

    @Test
    void shouldThrowExceptionWhenCustomerDoesNotExistWhileCreatingOrder() {
        // given
        var nonExistentCustomerId = UUID.randomUUID();
        var createOrderDto = getCreateOrderDto(nonExistentCustomerId);

        // expected
        assertThatThrownBy(() -> orderService.createOrder(createOrderDto))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessage(CustomerNotFoundException.createExceptionMessage(nonExistentCustomerId));
    }

    @Test
    void shouldPaidOrder() {
        // given
        var customer = new Customer();
        customer.setId(UUID.randomUUID());
        customer.setFirstName("Arnold");
        customer.setLastName("Boczek");
        customer.setEmail("boczek@gmail.com");
        var customerId = customerRepository.save(customer).getId();
        var createOrderDto = getCreateOrderDto(customerId);
        var orderId = orderService.createOrder(createOrderDto);

        // when
        orderService.pay(orderId);

        // then
        var paidOrder = orderRepository.findById(orderId).orElseThrow();
        assertThat(paidOrder.getStatus()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    void shouldThrowExceptionWhenOrderDoesNotExistForPayment() {
        // given
        var nonExistentOrderId = UUID.randomUUID();

        // expected
        assertThatThrownBy(() -> orderService.pay(nonExistentOrderId))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessage(OrderNotFoundException.createExceptionMessage(nonExistentOrderId));
    }

    @Test
    void shouldGetOrder() {
        // given
        var customer = new Customer();
        customer.setId(UUID.randomUUID());
        customer.setFirstName("Wadek");
        customer.setLastName("Kiepski");
        customer.setEmail("waldeczek@gmail.com");
        var customerId = customerRepository.save(customer).getId();
        var createOrderDto = getCreateOrderDto(customerId);
        var orderId = orderService.createOrder(createOrderDto);

        // when
        var order = orderService.getOrderById(orderId);

        // then
        assertThat(order)
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("customer.id", createOrderDto.customerId())
                .hasFieldOrPropertyWithValue("price", createOrderDto.price())
                .hasFieldOrPropertyWithValue("status", OrderStatus.PENDING)
                .extracting(Order::getAddress)
                .hasFieldOrPropertyWithValue("street", createOrderDto.address().street())
                .hasFieldOrPropertyWithValue("postalCode", createOrderDto.address().postalCode())
                .hasFieldOrPropertyWithValue("city", createOrderDto.address().city())
                .hasFieldOrPropertyWithValue("houseNo", createOrderDto.address().houseNo());

        assertThat(order.getItems()).hasSize(createOrderDto.items().size())
                .zipSatisfy(createOrderDto.items(), (orderItem, orderItemDto) -> {
                    assertThat(orderItem.getProductId()).isEqualTo(orderItemDto.productId());
                    assertThat(orderItem.getPrice()).isEqualTo(orderItemDto.price());
                    assertThat(orderItem.getQuantity()).isEqualTo(orderItemDto.quantity());
                    assertThat(orderItem.getTotalPrice()).isEqualTo(orderItemDto.totalPrice());
                });
    }

    @Test
    void shouldThrowExceptionWhenOrderDoesNotExist() {
        // given
        var nonExistentOrderId = UUID.randomUUID();

        // expected
        assertThatThrownBy(() -> orderService.getOrderById(nonExistentOrderId))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessage(OrderNotFoundException.createExceptionMessage(nonExistentOrderId));
    }

    private CreateOrderDto getCreateOrderDto(UUID customerId) {
        var items = List.of(new OrderItemDto(UUID.randomUUID(), 2, new BigDecimal("10.00"), new BigDecimal("20.00")),
                new OrderItemDto(UUID.randomUUID(), 1, new BigDecimal("34.56"), new BigDecimal("34.56")));
        var address = new OrderAddressDto("Małysza", "94-000", "Adasiowo", "12");
        return new CreateOrderDto(customerId, new BigDecimal("54.56"), items, address);
    }
}