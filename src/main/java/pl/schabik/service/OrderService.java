package pl.schabik.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schabik.controller.CreateOrderDto;
import pl.schabik.controller.OrderItemDto;
import pl.schabik.exception.CustomerNotFoundException;
import pl.schabik.exception.OrderInIncorrectStateException;
import pl.schabik.exception.OrderNotFoundException;
import pl.schabik.model.Order;
import pl.schabik.model.OrderAddress;
import pl.schabik.model.OrderItem;
import pl.schabik.model.OrderStatus;
import pl.schabik.repository.CustomerRepository;
import pl.schabik.repository.OrderRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public UUID createOrder(CreateOrderDto createOrderDto) {
        var customer = customerRepository.findById(createOrderDto.customerId()).orElseThrow(() ->
                new CustomerNotFoundException(createOrderDto.customerId()));

        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setCreateAt(Instant.now());
        order.setLastUpdateAt(Instant.now());
        order.setStatus(OrderStatus.PENDING);
        order.setCustomer(customer);
        setOrderItemsIfCorrect(createOrderDto, order);
        order.setPrice(createOrderDto.price().setScale(2, RoundingMode.HALF_EVEN));
        serAddressIfExists(createOrderDto, order);
        return orderRepository.save(order).getId();
    }

    private void setOrderItemsIfCorrect(CreateOrderDto createOrderDto, Order order) {
        int itemId = 1;
        List<OrderItem> items = new ArrayList<>();
        for (var itemDto : createOrderDto.items()) {
            validateItemPrice(itemDto);
            OrderItem orderItem = new OrderItem();
            orderItem.setId(itemId++);
            orderItem.setOrder(order);
            orderItem.setProductId(itemDto.productId());
            orderItem.setPrice(itemDto.price().setScale(2, RoundingMode.HALF_EVEN));
            orderItem.setQuantity(itemDto.quantity());
            orderItem.setTotalPrice(itemDto.totalPrice().setScale(2, RoundingMode.HALF_EVEN));
            items.add(orderItem);
        }
        order.setItems(items);
    }

    private void validateItemPrice(OrderItemDto itemDto) {
        if (!(itemDto.price().compareTo(BigDecimal.ZERO) > 0 &&
                itemDto.price().multiply(BigDecimal.valueOf(itemDto.quantity())).equals(itemDto.totalPrice()))) {
            throw new IllegalArgumentException("Incorrect order item price: " + itemDto.totalPrice());
        }
    }

    private void serAddressIfExists(CreateOrderDto createOrderDto, Order order) {
        if (createOrderDto.address() != null) {
            var orderAddress = new OrderAddress();
            orderAddress.setId(UUID.randomUUID());
            orderAddress.setCity(createOrderDto.address().city());
            orderAddress.setStreet(createOrderDto.address().street());
            orderAddress.setPostalCode(createOrderDto.address().postalCode());
            orderAddress.setHouseNo(createOrderDto.address().houseNo());
            order.setAddress(orderAddress);
        }
    }

    //TODO
    @Transactional
    public void pay(UUID orderId) {
        var order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        if (!OrderStatus.PENDING.equals(order.getStatus())) {
            throw new OrderInIncorrectStateException(orderId, order.getStatus());
        }
        order.setStatus(OrderStatus.PAID);
    }

    @Transactional(readOnly = true)
    public Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
    }
}
