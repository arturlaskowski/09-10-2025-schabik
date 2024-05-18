package pl.schabik.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schabik.controller.CreateOrderDto;
import pl.schabik.controller.OrderAddressDto;
import pl.schabik.controller.OrderItemDto;
import pl.schabik.exception.CustomerNotFoundException;
import pl.schabik.exception.OrderNotFoundException;
import pl.schabik.model.Customer;
import pl.schabik.model.Order;
import pl.schabik.model.OrderAddress;
import pl.schabik.model.OrderItem;
import pl.schabik.repository.CustomerRepository;
import pl.schabik.repository.OrderRepository;

import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public UUID createOrder(CreateOrderDto createOrderDto) {
        var customer = findCustomerById(createOrderDto.customerId());
        var items = convertToOrderItems(createOrderDto.items());
        var orderAddress = createOrderAddress(createOrderDto.address());

        var order = new Order(customer, createOrderDto.price().setScale(2, RoundingMode.HALF_EVEN),
                items, orderAddress);

        return orderRepository.save(order).getId();
    }


    //TODO
    @Transactional
    public void pay(UUID orderId) {
        var order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        order.pay();
    }

    @Transactional(readOnly = true)
    public Order getOrderById(UUID orderId) {
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
                        itemDto.price().setScale(2, RoundingMode.HALF_EVEN),
                        itemDto.quantity(),
                        itemDto.totalPrice().setScale(2, RoundingMode.HALF_EVEN)
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
