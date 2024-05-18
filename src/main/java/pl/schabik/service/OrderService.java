package pl.schabik.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schabik.controller.CreateOrderDto;
import pl.schabik.controller.OrderAddressDto;
import pl.schabik.controller.OrderItemDto;
import pl.schabik.exception.CustomerNotFoundException;
import pl.schabik.exception.OrderNotFoundException;
import pl.schabik.model.*;
import pl.schabik.repository.CustomerRepository;
import pl.schabik.repository.OrderRepository;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    public OrderId createOrder(CreateOrderDto createOrderDto) {
        var customer = findCustomerById(createOrderDto.customerId());
        var items = convertToOrderItems(createOrderDto.items());
        var orderAddress = createOrderAddress(createOrderDto.address());

        var order = new Order(customer, new Money(createOrderDto.price()),
                items, orderAddress);

        return orderRepository.save(order).getId();
    }

    //TODO
    @Transactional
    public void pay(OrderId orderId) {
        var order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        order.pay();
    }

    @Transactional(readOnly = true)
    public Order getOrderById(OrderId orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    private Customer findCustomerById(UUID customerId) throws CustomerNotFoundException {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    private List<OrderItem> convertToOrderItems(List<OrderItemDto> itemDtos) {
        return itemDtos.stream()
                .map(itemDto -> new OrderItem(
                        itemDto.productId(),
                        new Money(itemDto.price()),
                        new Quantity(itemDto.quantity()),
                        new Money(itemDto.totalPrice())
                )).toList();
    }

    private OrderAddress createOrderAddress(OrderAddressDto addressDto) {
        if (addressDto != null) {
            return new OrderAddress(
                    addressDto.street(),
                    addressDto.postalCode(),
                    addressDto.city(),
                    addressDto.houseNo()
            );
        }
        return null;
    }
}
