package pl.schabik.order.command.approve;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schabik.common.command.CommandHandler;
import pl.schabik.order.command.exception.OrderNotFoundException;
import pl.schabik.order.domain.OrderRepository;

@Service
public class ApproveOrderHandler implements CommandHandler<ApproveOrderCommand> {

    private final OrderRepository orderRepository;

    public ApproveOrderHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void handle(ApproveOrderCommand approveOrderCommand) {
        var order = orderRepository.findById(approveOrderCommand.orderId())
                .orElseThrow(() -> new OrderNotFoundException(approveOrderCommand.orderId()));

        order.approve();
    }
}
