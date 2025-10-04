package pl.schabik.order.infrastructure;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.schabik.order.application.OrderService;
import pl.schabik.order.domain.OrderId;
import pl.schabik.order.infrastructure.dto.CreateOrderRequest;
import pl.schabik.order.infrastructure.dto.GetOrderResponse;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody @Valid CreateOrderRequest createOrderRequest) {
        var createOrderDto = OrderApiMapper.mapToDto(createOrderRequest);
        var orderId = orderService.createOrder(createOrderDto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(orderId.id())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public GetOrderResponse getOrder(@PathVariable UUID id) {
        var orderDto = orderService.getOrderById(new OrderId(id));
        return OrderApiMapper.mapToGetOrderResponse(orderDto);
    }
}
