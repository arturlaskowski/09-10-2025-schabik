package pl.schabik.order.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schabik.order.domain.OrderStatus;
import pl.schabik.order.domain.vo.OrderId;
import pl.schabik.order.query.dto.OrderByIdQuery;
import pl.schabik.order.query.dto.OrderPageQuery;
import pl.schabik.order.query.exception.OrderQueryNotFoundException;

@Service
public class OrderQueryService {

    private final OrderQueryRepository orderQueryRepository;

    public OrderQueryService(OrderQueryRepository orderQueryRepository) {
        this.orderQueryRepository = orderQueryRepository;
    }

    @Transactional(readOnly = true)
    public OrderByIdQuery getOrderById(OrderId orderId) {
        var order = orderQueryRepository.findById(orderId).orElseThrow(() -> new OrderQueryNotFoundException(orderId));
        return new OrderByIdQuery(orderId.id(), order.getCustomerId(), order.getPrice().amount(), order.getStatus().name(),
                OrderQueryMapper.convertToOrderItemsDto(order.getItems()), OrderQueryMapper.convertToOrderAddressDto(order.getAddress()));
    }

    public Page<OrderPageQuery> findAllOrders(Pageable pageable) {
        return orderQueryRepository.findAll(pageable)
                .map(OrderQueryMapper::convertToOrderPageQuery);
    }

    ///  Demonstracja optymalizacji - tak powinna być zaimplementowana metoda findAllOrders
    public Page<OrderPageQuery> findOrdersByStatus(OrderStatus status, Pageable pageable) {
        return orderQueryRepository.findAllByStatus(status, pageable);
    }
}
