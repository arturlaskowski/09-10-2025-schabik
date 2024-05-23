package pl.schabik.order.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schabik.order.application.dto.CreateOrderDto;
import pl.schabik.order.application.dto.OrderDto;
import pl.schabik.order.application.exception.CustomerNotFoundException;
import pl.schabik.order.application.exception.OrderNotFoundException;
import pl.schabik.order.application.replication.CustomerProjectionService;
import pl.schabik.order.domain.Money;
import pl.schabik.order.domain.Order;
import pl.schabik.order.domain.OrderId;
import pl.schabik.order.domain.OrderRepository;

import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerProjectionService customerProjectionService;

    public OrderService(OrderRepository orderRepository, CustomerProjectionService customerProjectionService) {
        this.orderRepository = orderRepository;
        this.customerProjectionService = customerProjectionService;
    }

    public OrderId createOrder(CreateOrderDto createOrderDto) {
        validateCustomerExists(createOrderDto.customerId());
        var items = OrderMapper.convertToCreateOrderItems(createOrderDto.items());
        var orderAddress = OrderMapper.convertToCreateOrderAddress(createOrderDto.address());

        var order = new Order(createOrderDto.customerId(), new Money(createOrderDto.price()),
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
    public OrderDto getOrderById(OrderId orderId) {
        var order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        return new OrderDto(orderId.id(), order.getCustomerId(), order.getPrice().amount(), order.getStatus(),
                OrderMapper.convertToOrderItemsDto(order.getItems()), OrderMapper.convertToOrderAddressDto(order.getAddress()));
    }

    private void validateCustomerExists(UUID customerId) {
        if (!customerProjectionService.existsById(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }
    }
}
