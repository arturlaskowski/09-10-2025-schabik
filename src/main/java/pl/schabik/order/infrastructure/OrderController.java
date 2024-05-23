package pl.schabik.order.infrastructure;


import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import pl.schabik.order.application.OrderService;
import pl.schabik.order.domain.vo.OrderId;
import pl.schabik.order.infrastructure.dto.CreateOrderRequest;
import pl.schabik.order.infrastructure.dto.GetOrderResponse;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public UUID createOrder(@RequestBody @Valid CreateOrderRequest createOrderRequest) {
        var createOrderDto = OrderApiMapper.mapToDto(createOrderRequest);
        return orderService.createOrder(createOrderDto).id();
    }

    @GetMapping("/{id}")
    public GetOrderResponse getOrder(@PathVariable UUID id) {
        var orderDto = orderService.getOrderById(new OrderId(id));
        return OrderApiMapper.mapToGetOrderResponse(orderDto);
    }
}
