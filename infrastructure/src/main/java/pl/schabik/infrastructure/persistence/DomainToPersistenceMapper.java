package pl.schabik.infrastructure.persistence;

import pl.schabik.domain.Customer;
import pl.schabik.domain.Order;
import pl.schabik.domain.OrderAddress;
import pl.schabik.domain.OrderItem;

import java.util.List;

public class DomainToPersistenceMapper {

    private DomainToPersistenceMapper() {
    }

    public static JpaCustomerEntity toJpaEntity(Customer customer) {
        return new JpaCustomerEntity(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail()
        );
    }

    public static JpaOrderEntity toJpaEntity(Order order) {
        JpaCustomerEntity jpaCustomer = toJpaEntity(order.getCustomer());
        JpaOrderAddressEntity jpaAddress = toJpaEntity(order.getAddress());

        JpaOrderEntity jpaOrder = new JpaOrderEntity(
                order.getId().id(),
                order.getCreateAt(),
                order.getLastUpdateAt(),
                jpaCustomer,
                order.getPrice().amount(),
                order.getStatus(),
                jpaAddress,
                null
        );

        // Create items with reference to the JPA order entity
        List<JpaOrderItemEntity> jpaItems = order.getItems().stream()
                .map(item -> toJpaEntity(item, jpaOrder))
                .toList();

        // Set the items on the order
        jpaOrder.setItems(jpaItems);

        return jpaOrder;
    }

    public static JpaOrderAddressEntity toJpaEntity(OrderAddress address) {
        return new JpaOrderAddressEntity(
                address.getId(),
                address.getStreet(),
                address.getPostalCode(),
                address.getCity(),
                address.getHouseNo()
        );
    }

    public static JpaOrderItemEntity toJpaEntity(OrderItem item, JpaOrderEntity order) {
        return new JpaOrderItemEntity(
                item.getId(),
                order,
                item.getProductId(),
                item.getPrice().amount(),
                item.getQuantity().value(),
                item.getTotalPrice().amount()
        );
    }
}
