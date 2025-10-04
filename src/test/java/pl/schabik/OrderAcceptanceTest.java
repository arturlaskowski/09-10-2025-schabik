
package pl.schabik;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import pl.schabik.domain.*;
import pl.schabik.infrastructure.dto.CreateOrderRequest;

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
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("""
            given request to add order for existing customer,
            when request is sent,
            then save order and HTTP 200 status received""")
    void givenRequestToAddOrderForExistingCustomer_whenRequestIsSent_thenOrderSavedAndHttp200() {
        // given
        var createOrderRequest = createOrderRequest();

        // when
        ResponseEntity<UUID> response = restTemplate.postForEntity(getBaseUrl(), createOrderRequest, UUID.class);

        // then
        assertThat(response.getHeaders().getLocation()).isNotNull();
        var orderId = response.getHeaders().getLocation().getPath().split("/")[2];
        var savedOrder = orderRepository.findById(new OrderId(UUID.fromString(orderId))).orElseThrow();
        assertThat(savedOrder)
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("customer.id", createOrderRequest.customerId())
                .hasFieldOrPropertyWithValue("price", new Money(createOrderRequest.price()))
                .hasFieldOrPropertyWithValue("status", OrderStatus.PENDING)
                .extracting(Order::getAddress)
                .hasFieldOrPropertyWithValue("street", createOrderRequest.address().street())
                .hasFieldOrPropertyWithValue("postalCode", createOrderRequest.address().postalCode())
                .hasFieldOrPropertyWithValue("city", createOrderRequest.address().city())
                .hasFieldOrPropertyWithValue("houseNo", createOrderRequest.address().houseNo());

        assertThat(savedOrder.getItems()).hasSize(createOrderRequest.items().size())
                .zipSatisfy(createOrderRequest.items(), (orderItem, orderItemDto) -> {
                    assertThat(orderItem.getProductId()).isEqualTo(orderItemDto.productId());
                    assertThat(orderItem.getPrice()).isEqualTo(new Money(orderItemDto.price()));
                    assertThat(orderItem.getQuantity()).isEqualTo(new Quantity(orderItemDto.quantity()));
                    assertThat(orderItem.getTotalPrice()).isEqualTo(new Money(orderItemDto.totalPrice()));
                });
    }

    private CreateOrderRequest createOrderRequest() {
        var customerId = customerRepository.save(new Customer("Waldek", "Kiepski", "waldek@gmail.com")).getId();

        var items = List.of(new CreateOrderRequest.OrderItemRequest(UUID.randomUUID(), 2, new BigDecimal("10.00"), new BigDecimal("20.00")),
                new CreateOrderRequest.OrderItemRequest(UUID.randomUUID(), 1, new BigDecimal("34.56"), new BigDecimal("34.56")));
        var address = new CreateOrderRequest.OrderAddressRequest("Małysza", "94-000", "Adasiowo", "12");
        return new CreateOrderRequest(customerId, new BigDecimal("54.56"), items, address);
    }

    private String getBaseUrl() {
        return "http://localhost:" + port + "/orders";
    }
}