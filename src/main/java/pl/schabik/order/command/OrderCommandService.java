package pl.schabik.order.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schabik.order.command.exception.OrderNotFoundException;
import pl.schabik.order.replication.CustomerProjectionService;
import pl.schabik.order.command.dto.ApproveOrderCommand;
import pl.schabik.order.command.dto.CreateOrderCommand;
import pl.schabik.order.command.dto.PayOrderCommand;
import pl.schabik.order.command.exception.CustomerNotFoundException;
import pl.schabik.order.domain.Order;
import pl.schabik.order.domain.OrderRepository;
import pl.schabik.order.domain.vo.Money;
import pl.schabik.order.domain.vo.OrderId;

import java.util.UUID;

@Service
public class OrderCommandService {

    private final OrderRepository orderRepository;
    private final CustomerProjectionService customerProjectionService;

    public OrderCommandService(OrderRepository orderRepository, CustomerProjectionService customerProjectionService) {
        this.orderRepository = orderRepository;
        this.customerProjectionService = customerProjectionService;
    }

    public OrderId createOrder(CreateOrderCommand createOrderCommand) {
        validateCustomerExists(createOrderCommand.customerId());
        var items = OrderCommandMapper.convertToCreateOrderItems(createOrderCommand.items());
        var orderAddress = OrderCommandMapper.convertToCreateOrderAddress(createOrderCommand.address());

        var order = new Order(createOrderCommand.customerId(), new Money(createOrderCommand.price()),
                items, orderAddress);

        return orderRepository.save(order).getId();
    }

    @Transactional
    public void pay(PayOrderCommand payOrderCommand) {
        var order = orderRepository.findById(payOrderCommand.orderId()).orElseThrow(() -> new OrderNotFoundException(payOrderCommand.orderId()));
        order.pay();
    }

    @Transactional
    public void approve(ApproveOrderCommand approveOrderCommand) {
        var order = orderRepository.findById(approveOrderCommand.orderId())
                .orElseThrow(() -> new OrderNotFoundException(approveOrderCommand.orderId()));

        order.approve();
    }

    private void validateCustomerExists(UUID customerId) {
        if (!customerProjectionService.existsById(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }
    }
}
