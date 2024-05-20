package pl.schabik.usecase.payorder;


import jakarta.transaction.Transactional;
import pl.schabik.domain.OrderId;
import pl.schabik.domain.OrderRepository;
import pl.schabik.usecase.common.OrderNotFoundException;

public class PayOrderService {

    private final OrderRepository orderRepository;

    public PayOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void pay(OrderId orderId) {
        var order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        order.pay();
    }
}
