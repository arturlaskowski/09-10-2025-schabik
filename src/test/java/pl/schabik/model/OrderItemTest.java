package pl.schabik.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderItemTest {

    @Test
    void shouldCreateOrderItemWithValidParameters() {
        // given
        var productId = UUID.randomUUID();
        var price = new BigDecimal("10.00");
        var quantity = 2;
        var totalPrice = new BigDecimal("20.00");

        // when
        var orderItem = new OrderItem(productId, price, quantity, totalPrice);

        // then
        assertThat(orderItem.getProductId()).isEqualTo(productId);
        assertThat(orderItem.getPrice()).isEqualByComparingTo(price);
        assertThat(orderItem.getQuantity()).isEqualTo(quantity);
        assertThat(orderItem.getTotalPrice()).isEqualByComparingTo(totalPrice);
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNotPositive() {
        // given
        var productId = UUID.randomUUID();
        var negativePrice = new BigDecimal("-1.00");
        var quantity = 2;
        var totalPrice = new BigDecimal("-2.00");

        //when
        var orderDomainException = assertThrows(OrderDomainException.class,
                () -> new OrderItem(productId, negativePrice, quantity, totalPrice));

        //then
        assertEquals("Price must be greater than zero but was: " + negativePrice, orderDomainException.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenTotalPriceDoesNotMatchCalculatedPrice() {
        // given
        var productId = UUID.randomUUID();
        var price = new BigDecimal("10.00");
        var quantity = 2;
        var incorrectTotalPrice = new BigDecimal("25.00");

        //when
        var orderDomainException = assertThrows(OrderDomainException.class,
                () -> new OrderItem(productId, price, quantity, incorrectTotalPrice));

        //then
        assertEquals("Total price should be equal to price multiplied by quantity. Expected: " +
                price.multiply(BigDecimal.valueOf(quantity)) + " but was: " + incorrectTotalPrice, orderDomainException.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsNotPositive() {
        // given
        var productId = UUID.randomUUID();
        var price = new BigDecimal("10.00");
        var negativeQuantity = -1;
        var totalPrice = new BigDecimal("10.00");

        //when
        var orderDomainException = assertThrows(OrderDomainException.class,
                () -> new OrderItem(productId, price, negativeQuantity, totalPrice));

        //then
        assertEquals("Quantity must be non-negative but was: " + negativeQuantity, orderDomainException.getMessage());
    }
}

