package pl.schabik.application;

import pl.schabik.application.dto.CreateOrderAddressDto;
import pl.schabik.application.dto.CreateOrderItemDto;
import pl.schabik.application.dto.OrderAddressDto;
import pl.schabik.application.dto.OrderItemDto;
import pl.schabik.domain.Money;
import pl.schabik.domain.OrderAddress;
import pl.schabik.domain.OrderItem;
import pl.schabik.domain.Quantity;

import java.util.List;

public class OrderMapper {

    public static List<OrderItem> convertToCreateOrderItems(List<CreateOrderItemDto> itemDtos) {
        return itemDtos.stream()
                .map(itemDto -> new OrderItem(
                        itemDto.productId(),
                        new Money(itemDto.price()),
                        new Quantity(itemDto.quantity()),
                        new Money(itemDto.totalPrice())
                )).toList();
    }

    public static OrderAddress convertToCreateOrderAddress(CreateOrderAddressDto addressDto) {
        if (addressDto != null) {
            return new OrderAddress(
                    addressDto.street(),
                    addressDto.postalCode(),
                    addressDto.city(),
                    addressDto.houseNo()
            );
        }
        return null;
    }

    public static List<OrderItemDto> convertToOrderItemsDto(List<OrderItem> items) {
        return items.stream()
                .map(item -> new OrderItemDto(
                        item.getProductId(),
                        item.getQuantity().value(),
                        item.getPrice().amount(),
                        item.getTotalPrice().amount()
                )).toList();
    }

    public static OrderAddressDto convertToOrderAddressDto(OrderAddress orderAddress) {
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
}
