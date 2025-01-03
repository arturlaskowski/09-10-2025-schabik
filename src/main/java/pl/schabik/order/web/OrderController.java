package pl.schabik.order.web;


import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.schabik.order.command.OrderCommandService;
import pl.schabik.order.command.dto.ApproveOrderCommand;
import pl.schabik.order.command.dto.PayOrderCommand;
import pl.schabik.order.domain.OrderStatus;
import pl.schabik.order.domain.vo.OrderId;
import pl.schabik.order.query.OrderQueryService;
import pl.schabik.order.query.dto.OrderByIdQuery;
import pl.schabik.order.query.dto.OrderPageQuery;
import pl.schabik.order.web.dto.CreateOrderRequest;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
class OrderController {

    private final OrderCommandService orderCommandService;
    private final OrderQueryService orderQueryService;

    public OrderController(OrderCommandService orderCommandService, OrderQueryService orderQueryService) {
        this.orderCommandService = orderCommandService;
        this.orderQueryService = orderQueryService;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody @Valid CreateOrderRequest createOrderRequest) {
        var createOrderDto = OrderApiMapper.mapToDto(createOrderRequest);
        var orderId = orderCommandService.createOrder(createOrderDto).id();

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(orderId)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<Void> payOrder(@PathVariable UUID id) {
        var payOrderCommand = new PayOrderCommand(new OrderId(id));
        orderCommandService.pay(payOrderCommand);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approveOrder(@PathVariable UUID id) {
        var approveOrderCommand = new ApproveOrderCommand(new OrderId(id));
        orderCommandService.approve(approveOrderCommand);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public OrderByIdQuery getOrder(@PathVariable UUID id) {
        return orderQueryService.getOrderById(new OrderId(id));
    }

    @GetMapping
    public ResponseEntity<Page<OrderPageQuery>> getAllOrders(
            @SortDefault(sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<OrderPageQuery> orders = orderQueryService.findAllOrders(pageable);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/pending")
    public ResponseEntity<Page<OrderPageQuery>> getPendingOrders(
            @SortDefault(sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<OrderPageQuery> pendingOrders = orderQueryService.findOrdersByStatus(OrderStatus.PENDING, pageable);
        return ResponseEntity.ok(pendingOrders);
    }
}
