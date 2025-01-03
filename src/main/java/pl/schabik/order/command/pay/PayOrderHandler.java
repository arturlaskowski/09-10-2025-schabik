package pl.schabik.order.command.pay;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schabik.common.command.CommandHandler;
import pl.schabik.order.command.exception.OrderNotFoundException;
import pl.schabik.order.domain.OrderRepository;

@Service
public class PayOrderHandler implements CommandHandler<PayOrderCommand> {

    private final OrderRepository orderRepository;

    public PayOrderHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void handle(PayOrderCommand payOrderCommand) {
        var order = orderRepository.findById(payOrderCommand.orderId())
                .orElseThrow(() -> new OrderNotFoundException(payOrderCommand.orderId()));
        order.pay();
    }
}
