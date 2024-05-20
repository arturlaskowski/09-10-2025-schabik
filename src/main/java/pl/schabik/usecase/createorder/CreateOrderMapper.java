package pl.schabik.usecase.createorder;

import pl.schabik.domain.Money;
import pl.schabik.domain.OrderAddress;
import pl.schabik.domain.OrderItem;
import pl.schabik.domain.Quantity;

import java.util.List;

public class CreateOrderMapper {

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
}
