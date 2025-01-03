package pl.schabik.order.web;


import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.schabik.common.command.CommandHandlerExecutor;
import pl.schabik.order.command.approve.ApproveOrderCommand;
import pl.schabik.order.command.pay.PayOrderCommand;
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

    private final CommandHandlerExecutor commandHandlerExecutor;
    private final OrderQueryService orderQueryService;

    OrderController(CommandHandlerExecutor commandHandlerExecutor, OrderQueryService orderQueryService) {
        this.commandHandlerExecutor = commandHandlerExecutor;
        this.orderQueryService = orderQueryService;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody @Valid CreateOrderRequest createOrderRequest) {
        var orderId = OrderId.newOne();
        var createOrderCommand = OrderApiMapper.mapToOrderCommand(orderId, createOrderRequest);
        commandHandlerExecutor.execute(createOrderCommand);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(orderId.id())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<Void> payOrder(@PathVariable UUID id) {
        var payOrderCommand = new PayOrderCommand(new OrderId(id));
        commandHandlerExecutor.execute(payOrderCommand);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approveOrder(@PathVariable UUID id) {
        var approveOrderCommand = new ApproveOrderCommand(new OrderId(id));
        commandHandlerExecutor.execute(approveOrderCommand);

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
