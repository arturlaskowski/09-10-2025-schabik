package pl.schabik.infrastructure;


import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.schabik.usecase.createorder.CreateOrderService;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CreateOrderService createOrderService;

    public OrderController(CreateOrderService createOrderService) {
        this.createOrderService = createOrderService;
    }

    @PostMapping
    public UUID createOrder(@RequestBody @Valid CreateOrderRequest createOrderRequest) {
        var createOrderDto = OrderApiMapper.mapToDto(createOrderRequest);
        return createOrderService.createOrder(createOrderDto).id();
    }
}
