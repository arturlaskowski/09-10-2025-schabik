package pl.schabik.order.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import pl.schabik.order.domain.Order;
import pl.schabik.order.domain.OrderStatus;
import pl.schabik.order.domain.vo.OrderId;
import pl.schabik.order.query.dto.OrderPageQuery;


interface OrderQueryRepository extends JpaRepository<Order, OrderId>, JpaSpecificationExecutor<Order> {

    @Query("SELECT new pl.schabik.order.query.dto.OrderPageQuery(o.id.orderId, o.createAt, o.status, o.price.amount)" +
            " FROM Order o WHERE o.status = :status")
    Page<OrderPageQuery> findAllByStatus(OrderStatus status, Pageable pageable);
}
