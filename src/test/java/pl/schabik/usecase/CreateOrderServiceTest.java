package pl.schabik.usecase;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import pl.schabik.domain.*;
import pl.schabik.usecase.createorder.CreateOrderAddressDto;
import pl.schabik.usecase.createorder.CreateOrderDto;
import pl.schabik.usecase.createorder.CreateOrderItemDto;
import pl.schabik.usecase.createorder.CreateOrderService;
import pl.schabik.usecase.getcustomer.CustomerNotFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CreateOrderServiceTest {

    InMemoryOrderRepository orderRepository = new InMemoryOrderRepository();
    InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository();
    CreateOrderService createOrderService = new CreateOrderService(orderRepository, customerRepository);

    @AfterEach
    void cleanUp() {
        orderRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void shouldCreateOrder() {
        // given
        var customerId = customerRepository.save(new Customer("Arnold", "Boczek", "boczek@gmail.com")).getId();
        var createOrderDto = getCreateOrderDto(customerId);

        // when
        var orderId = createOrderService.createOrder(createOrderDto);

        // then
        var savedOrder = orderRepository.findById(orderId).orElseThrow();
        assertThat(savedOrder)
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("customer.id", createOrderDto.customerId())
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
        var createOrderDto = getCreateOrderDto(nonExistentCustomerId);

        // expected
        assertThatThrownBy(() -> createOrderService.createOrder(createOrderDto))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessage(CustomerNotFoundException.createExceptionMessage(nonExistentCustomerId));
    }

    private CreateOrderDto getCreateOrderDto(UUID customerId) {
        var items = List.of(new CreateOrderItemDto(UUID.randomUUID(), 2, new BigDecimal("10.00"), new BigDecimal("20.00")),
                new CreateOrderItemDto(UUID.randomUUID(), 1, new BigDecimal("34.56"), new BigDecimal("34.56")));
        var address = new CreateOrderAddressDto("Małysza", "94-000", "Adasiowo", "12");
        return new CreateOrderDto(customerId, new BigDecimal("54.56"), items, address);
    }
}