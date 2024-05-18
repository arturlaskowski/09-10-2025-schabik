package pl.schabik.model;


import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void shouldCreateOrderWithValidDetails() {
        //given
        var customer = new Customer();
        var item = new OrderItem(UUID.randomUUID(), new BigDecimal("10.00"), 2, new BigDecimal("20.00"));
        var item2 = new OrderItem(UUID.randomUUID(), new BigDecimal("15.50"), 3, new BigDecimal("46.50"));
        var address = new OrderAddress("Boczka", "12345", "Arnoldowo", "1A");
        var beforeCreation = Instant.now();

        //when
        var order = new Order(customer, new BigDecimal("66.50"), List.of(item, item2), address);
        var afterCreation = Instant.now();

        // then
        assertThat(order.getCustomer()).isEqualTo(customer);
        assertThat(order.getPrice()).isEqualByComparingTo("66.50");
        assertThat(order.getItems()).containsExactlyInAnyOrder(item, item2);
        assertThat(order.getAddress()).isEqualTo(address);
        assertTrue(order.isPendingStatus());
        assertThat(order.getCreateAt())
                .isNotNull()
                .isAfterOrEqualTo(beforeCreation)
                .isBeforeOrEqualTo(afterCreation);
        assertThat(order.getLastUpdateAt())
                .isNotNull()
                .isAfterOrEqualTo(beforeCreation)
                .isBeforeOrEqualTo(afterCreation);
    }

    @Test
    void shouldThrowExceptionWhenCreatingOrderWithNegativePrice() {
        //given
        var customer = new Customer();
        var items = List.of(new OrderItem(UUID.randomUUID(), new BigDecimal("10.00"), 2, new BigDecimal("20.00")));
        var address = new OrderAddress("Boczka", "12345", "Arnoldowo", "1A");
        var priceLowerThenZero = new BigDecimal("-1");

        //when
        var orderDomainException = assertThrows(OrderDomainException.class,
                () -> new Order(customer, priceLowerThenZero, items, address));

        //then
        assertEquals("Order price: " + priceLowerThenZero + " must be greater than zero", orderDomainException.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenOrderPriceDoesNotMatchItemTotals() {
        //given
        var customer = new Customer();
        var sumOfOrderItemsPrice = new BigDecimal("20.00");
        var items = List.of(new OrderItem(UUID.randomUUID(), new BigDecimal("10.00"), 2, sumOfOrderItemsPrice));
        var address = new OrderAddress("Boczka", "12345", "Arnoldowo", "1A");
        var differentPriceThanSumOrderItems = new BigDecimal("14.56");

        //when
        var orderDomainException = assertThrows(OrderDomainException.class,
                () -> new Order(customer, differentPriceThanSumOrderItems, items, address));

        //then
        assertEquals("Total order price: " + differentPriceThanSumOrderItems +
                " is different than order items total: " + sumOfOrderItemsPrice, orderDomainException.getMessage());
    }

    @Test
    void shouldAllowPaymentWhenOrderStatusIsPending() {
        //given
        var order = createOrder();
        var now = Instant.now();

        //when
        order.pay();

        //then
        assertTrue(order.isPaidStatus());
        assertThat(order.getLastUpdateAt()).isAfter(now);

    }

    @Test
    void shouldThrowExceptionWhenPayingNonPendingOrder() {
        //given
        var order = createOrder();
        order.pay();

        //when
        var orderDomainException = assertThrows(OrderDomainException.class, order::pay);

        //then
        assertEquals("Order is not in correct state for pay operation", orderDomainException.getMessage());
    }

    private Order createOrder() {
        var customer = new Customer();
        var item = new OrderItem(UUID.randomUUID(), new BigDecimal("10.00"), 2, new BigDecimal("20.00"));
        var address = new OrderAddress("Boczka", "12345", "Arnoldowo", "1A");
        return new Order(customer, new BigDecimal("20.00"), List.of(item), address);
    }
}