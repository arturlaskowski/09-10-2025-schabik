package pl.schabik.infrastructure;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.schabik.domain.OrderId;
import pl.schabik.infrastructure.dto.CreateOrderRequest;
import pl.schabik.infrastructure.dto.GetOrderResponse;
import pl.schabik.usecase.createorder.CreateOrderService;
import pl.schabik.usecase.getorder.GetOrderService;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CreateOrderService createOrderService;
    private final GetOrderService getOrderService;

    public OrderController(CreateOrderService createOrderService, GetOrderService getOrderService) {
        this.createOrderService = createOrderService;
        this.getOrderService = getOrderService;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody @Valid CreateOrderRequest createOrderRequest) {
        var createOrderDto = OrderApiMapper.mapToDto(createOrderRequest);
        var orderId = createOrderService.createOrder(createOrderDto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(orderId.id())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public GetOrderResponse getOrder(@PathVariable UUID id) {
        var orderDto = getOrderService.getOrderById(new OrderId(id));
        return OrderApiMapper.mapToGetOrderResponse(orderDto);
    }
}
