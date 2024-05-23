
package pl.schabik;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import pl.schabik.common.ErrorResponse;
import pl.schabik.customer.CreateCustomerRequest;
import pl.schabik.order.application.dto.CreateOrderAddressDto;
import pl.schabik.order.application.dto.CreateOrderDto;
import pl.schabik.order.application.dto.CreateOrderItemDto;
import pl.schabik.order.domain.OrderStatus;
import pl.schabik.order.infrastructure.dto.GetOrderResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateOrderEndToEndTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("""
            given add customer and add order for existing customer,
            when request is sent,
            then save order and HTTP 200 status received""")
    void givenRequestToAddOrderForExistingCustomer_whenRequestIsSent_thenOrderSavedAndHttp200() {
        //given
        var createCustomerDto = new CreateCustomerRequest("Marianek", "Paździoch", "pazdzeik@gemail.com");

        //when - create customer
        var postCustomerResponse = restTemplate.postForEntity(getBaseCustomersUrl(), createCustomerDto, UUID.class);
        assertThat(postCustomerResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(postCustomerResponse.getBody()).isNotNull();

        //when - create order
        var customerId = postCustomerResponse.getBody();
        var createOrderDto = createOrderDto(customerId);
        var postOrderResponse = restTemplate.postForEntity(getBaseOrdersUrl(), createOrderDto, UUID.class);
        assertThat(postOrderResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(postOrderResponse.getBody()).isNotNull();

        //when - get order
        var getOrderResponse = restTemplate.getForEntity(
                getBaseOrdersUrl() + "/" + postOrderResponse.getBody(), GetOrderResponse.class);
        assertThat(getOrderResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getOrderResponse.getBody()).isNotNull();

        //then
        var orderResponse = getOrderResponse.getBody();
        assertThat(orderResponse)
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("customerId", createOrderDto.customerId())
                .hasFieldOrPropertyWithValue("price", createOrderDto.price())
                .hasFieldOrPropertyWithValue("status", OrderStatus.PENDING.name())
                .extracting(GetOrderResponse::address)
                .hasFieldOrPropertyWithValue("street", createOrderDto.address().street())
                .hasFieldOrPropertyWithValue("postalCode", createOrderDto.address().postalCode())
                .hasFieldOrPropertyWithValue("city", createOrderDto.address().city())
                .hasFieldOrPropertyWithValue("houseNo", createOrderDto.address().houseNo());

        assertThat(orderResponse.items()).hasSize(createOrderDto.items().size())
                .zipSatisfy(createOrderDto.items(), (getItem, postItem) -> {
                    assertThat(getItem.productId()).isEqualTo(postItem.productId());
                    assertThat(getItem.price()).isEqualTo(postItem.price());
                    assertThat(getItem.quantity()).isEqualTo(postItem.quantity());
                    assertThat(getItem.totalPrice()).isEqualTo(postItem.totalPrice());
                });
    }

    @Test
    @DisplayName("""
            add order for not existing customer,
            when request is sent,
            then order is not saved and HTTP 400 status received""")
    void givenRequestToAddOrderForNotExistingCustomer_whenRequestIsSent_thenOrderNotSavedAndHttp400() {
        //when - create order
        var notExistingCustomerId = UUID.randomUUID();
        var createOrderDto = createOrderDto(notExistingCustomerId);
        var postOrderResponse = restTemplate.postForEntity(getBaseOrdersUrl(), createOrderDto, ErrorResponse.class);

        //then
        assertThat(postOrderResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(postOrderResponse.getBody())
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .extracting("message")
                .asString()
                .contains("Could not find customer");
    }

    private CreateOrderDto createOrderDto(UUID customerId) {
        var items = List.of(new CreateOrderItemDto(UUID.randomUUID(), 2, new BigDecimal("10.00"), new BigDecimal("20.00")),
                new CreateOrderItemDto(UUID.randomUUID(), 1, new BigDecimal("34.56"), new BigDecimal("34.56")));
        var address = new CreateOrderAddressDto("Małysza", "94-000", "Adasiowo", "12");
        return new CreateOrderDto(customerId, new BigDecimal("54.56"), items, address);
    }

    private String getBaseOrdersUrl() {
        return "http://localhost:" + port + "/orders";
    }

    private String getBaseCustomersUrl() {
        return "http://localhost:" + port + "/customers";
    }
}