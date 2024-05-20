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
import pl.schabik.usecase.getorder.GetOrderService;
import pl.schabik.usecase.getorder.OrderDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class GetOrderServiceTest {

    InMemoryOrderRepository orderRepository = new InMemoryOrderRepository();
    InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository();
    CreateOrderService createOrderService = new CreateOrderService(orderRepository, customerRepository);
    GetOrderService getOrderServiceTest = new GetOrderService(orderRepository);

    @AfterEach
    void cleanUp() {
        orderRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void shouldGetOrder() {
        // given
        var customerId = customerRepository.save(new Customer("Arnold", "Boczek", "boczek@gmail.com")).getId();
        var createOrderDto = getCreateOrderDto(customerId);
        var orderId = createOrderService.createOrder(createOrderDto);

        // when
        var orderDto = getOrderServiceTest.getOrderById(orderId);

        // then
        assertThat(orderDto)
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("customerId", createOrderDto.customerId())
                .hasFieldOrPropertyWithValue("price", createOrderDto.price())
                .hasFieldOrPropertyWithValue("status", OrderStatus.PENDING)
                .extracting(OrderDto::address)
                .hasFieldOrPropertyWithValue("street", createOrderDto.address().street())
                .hasFieldOrPropertyWithValue("postalCode", createOrderDto.address().postalCode())
                .hasFieldOrPropertyWithValue("city", createOrderDto.address().city())
                .hasFieldOrPropertyWithValue("houseNo", createOrderDto.address().houseNo());

        assertThat(orderDto.items()).hasSize(createOrderDto.items().size())
                .zipSatisfy(createOrderDto.items(), (dto, create) -> {
                    assertThat(dto.productId()).isEqualTo(create.productId());
                    assertThat(dto.price()).isEqualTo(create.price());
                    assertThat(dto.quantity()).isEqualTo(create.quantity());
                    assertThat(dto.totalPrice()).isEqualTo(create.totalPrice());
                });
    }

    @Test
    void shouldThrowExceptionWhenOrderDoesNotExist() {
        // given
        var nonExistentOrderId = new OrderId(UUID.randomUUID());

        // expected
        assertThatThrownBy(() -> getOrderServiceTest.getOrderById(nonExistentOrderId))
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