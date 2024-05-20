package pl.schabik.usecase.createorder;

import pl.schabik.domain.*;
import pl.schabik.usecase.getcustomer.CustomerNotFoundException;

import java.util.UUID;

import static pl.schabik.usecase.createorder.CreateOrderMapper.convertToCreateOrderAddress;
import static pl.schabik.usecase.createorder.CreateOrderMapper.convertToCreateOrderItems;

public class CreateOrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public CreateOrderService(OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    public OrderId createOrder(CreateOrderDto createOrderDto) {
        var customer = findCustomerById(createOrderDto.customerId());
        var items = convertToCreateOrderItems(createOrderDto.items());
        var orderAddress = convertToCreateOrderAddress(createOrderDto.address());

        var order = new Order(customer, new Money(createOrderDto.price()),
                items, orderAddress);

        return orderRepository.save(order).getId();
    }

    private Customer findCustomerById(UUID customerId) throws CustomerNotFoundException {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }
}
