package pl.schabik.order.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import pl.schabik.common.CustomerCreatedEvent;
import pl.schabik.order.application.OrderService;
import pl.schabik.order.application.dto.OrderDto;
import pl.schabik.order.application.replication.CustomerProjectionService;
import pl.schabik.order.domain.OrderId;
import pl.schabik.order.domain.OrderStatus;
import pl.schabik.order.infrastructure.dto.CreateOrderRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerProjectionService customerService;

    @Autowired
    private OrderService orderService;

    @Test
    @DisplayName("""
            given request to add order for existing customer,
            when request is sent,
            then save order and HTTP 200 status received""")
    void givenRequestToAddOrderForExistingCustomer_whenRequestIsSent_thenOrderSavedAndHttp200() {
        // given
        var createOrderRequest = createOrderRequest();

        // when
        ResponseEntity<Void> response = restTemplate.postForEntity(getBaseUrl(), createOrderRequest, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        var location = response.getHeaders().getLocation();
        assertThat(location).isNotNull();
        var orderId = UUID.fromString(UriComponentsBuilder.fromUri(response.getHeaders().getLocation()).build()
                .getPathSegments().getLast());

        // then
        var savedOrder = orderService.getOrderById(new OrderId(orderId));
        assertThat(savedOrder)
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("customerId", createOrderRequest.customerId())
                .hasFieldOrPropertyWithValue("price", createOrderRequest.price())
                .hasFieldOrPropertyWithValue("status", OrderStatus.PENDING)
                .extracting(OrderDto::address)
                .hasFieldOrPropertyWithValue("street", createOrderRequest.address().street())
                .hasFieldOrPropertyWithValue("postalCode", createOrderRequest.address().postalCode())
                .hasFieldOrPropertyWithValue("city", createOrderRequest.address().city())
                .hasFieldOrPropertyWithValue("houseNo", createOrderRequest.address().houseNo());

        assertThat(savedOrder.items()).hasSize(createOrderRequest.items().size())
                .zipSatisfy(createOrderRequest.items(), (orderItem, orderItemDto) -> {
                    assertThat(orderItem.productId()).isEqualTo(orderItemDto.productId());
                    assertThat(orderItem.price()).isEqualTo(orderItemDto.price());
                    assertThat(orderItem.quantity()).isEqualTo(orderItemDto.quantity());
                    assertThat(orderItem.totalPrice()).isEqualTo(orderItemDto.totalPrice());
                });
    }

    private CreateOrderRequest createOrderRequest() {
        var customerId = UUID.randomUUID();
        customerService.replicateCustomer(new CustomerCreatedEvent(customerId));

        var items = List.of(new CreateOrderRequest.OrderItemRequest(UUID.randomUUID(), 2, new BigDecimal("10.00"), new BigDecimal("20.00")),
                new CreateOrderRequest.OrderItemRequest(UUID.randomUUID(), 1, new BigDecimal("34.56"), new BigDecimal("34.56")));
        var address = new CreateOrderRequest.OrderAddressRequest("Małysza", "94-000", "Adasiowo", "12");
        return new CreateOrderRequest(customerId, new BigDecimal("54.56"), items, address);
    }

    private String getBaseUrl() {
        return "http://localhost:" + port + "/orders";
    }
}