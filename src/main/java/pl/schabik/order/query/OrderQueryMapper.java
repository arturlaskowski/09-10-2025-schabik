package pl.schabik.order.query;

import pl.schabik.order.domain.Order;
import pl.schabik.order.domain.OrderAddress;
import pl.schabik.order.domain.OrderItem;
import pl.schabik.order.query.dto.OrderAddressDto;
import pl.schabik.order.query.dto.OrderItemDto;
import pl.schabik.order.query.dto.OrderPageQuery;

import java.util.List;

class OrderQueryMapper {

    private OrderQueryMapper() {
    }

    static List<OrderItemDto> convertToOrderItemsDto(List<OrderItem> items) {
        return items.stream()
                .map(item -> new OrderItemDto(
                        item.getProductId(),
                        item.getQuantity().value(),
                        item.getPrice().amount(),
                        item.getTotalPrice().amount()
                )).toList();
    }

    static OrderAddressDto convertToOrderAddressDto(OrderAddress orderAddress) {
        if (orderAddress != null) {
            return new OrderAddressDto(
                    orderAddress.getStreet(),
                    orderAddress.getPostalCode(),
                    orderAddress.getCity(),
                    orderAddress.getHouseNo()
            );
        }
        return null;
    }

    static OrderPageQuery convertToOrderPageQuery(Order order) {
        return new OrderPageQuery(order.getId().id(), order.getCreateAt(),
                order.getStatus(), order.getPrice().amount());
    }
}
