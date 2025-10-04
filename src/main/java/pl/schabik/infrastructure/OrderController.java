package pl.schabik.infrastructure;


import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import pl.schabik.application.OrderService;
import pl.schabik.domain.OrderId;
import pl.schabik.infrastructure.dto.CreateOrderRequest;
import pl.schabik.infrastructure.dto.GetOrderResponse;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

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
