package pl.schabik.usecase.getorder;

import pl.schabik.domain.OrderId;
import pl.schabik.domain.OrderRepository;
import pl.schabik.usecase.common.OrderNotFoundException;

import static pl.schabik.usecase.getorder.GetOrderMapper.convertToOrderAddressDto;
import static pl.schabik.usecase.getorder.GetOrderMapper.convertToOrderItemsDto;

public class GetOrderService {

    private final OrderRepository orderRepository;

    public GetOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderDto getOrderById(OrderId orderId) {
        var order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        return new OrderDto(orderId.id(), order.getCustomer().getId(), order.getPrice().amount(), order.getStatus(),
                convertToOrderItemsDto(order.getItems()), convertToOrderAddressDto(order.getAddress()));
    }
}
