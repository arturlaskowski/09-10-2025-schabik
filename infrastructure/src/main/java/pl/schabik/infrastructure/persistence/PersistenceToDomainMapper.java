package pl.schabik.infrastructure.persistence;

import pl.schabik.domain.*;

import java.util.List;

public class PersistenceToDomainMapper {

    private PersistenceToDomainMapper() {
    }

    public static Customer toDomain(JpaCustomerEntity entity) {
        return new Customer(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail()
        );
    }

    public static Order toDomain(JpaOrderEntity entity) {
        Customer customer = toDomain(entity.getCustomer());
        OrderAddress address = toDomain(entity.getAddress());

        OrderId orderId = new OrderId(entity.getId());
        Money price = new Money(entity.getPrice());

        List<OrderItem> items = entity.getItems().stream()
                .map(PersistenceToDomainMapper::toDomain)
                .toList();

        Order order = new Order(
                orderId,
                entity.getCreateAt(),
                entity.getLastUpdateAt(),
                customer,
                price,
                entity.getStatus(),
                address,
                items
        );

        // Initialize the order items with their ids and order reference
        for (int i = 0; i < items.size(); i++) {
            JpaOrderItemEntity jpaItem = entity.getItems().get(i);
            items.get(i).initializeBasketItem(order, jpaItem.getId());
        }

        return order;
    }

    public static OrderAddress toDomain(JpaOrderAddressEntity entity) {
        return new OrderAddress(
                entity.getId(),
                entity.getStreet(),
                entity.getPostalCode(),
                entity.getCity(),
                entity.getHouseNo()
        );
    }

    public static OrderItem toDomain(JpaOrderItemEntity entity) {
        Money price = new Money(entity.getPrice());
        Quantity quantity = new Quantity(entity.getQuantity());
        Money totalPrice = new Money(entity.getTotalPrice());

        return new OrderItem(
                entity.getProductId(),
                price,
                quantity,
                totalPrice
        );
    }
}
