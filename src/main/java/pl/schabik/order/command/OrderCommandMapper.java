package pl.schabik.order.command;

import pl.schabik.order.command.dto.CreateOrderAddressDto;
import pl.schabik.order.command.dto.CreateOrderItemDto;
import pl.schabik.order.domain.OrderAddress;
import pl.schabik.order.domain.OrderItem;
import pl.schabik.order.domain.vo.Money;
import pl.schabik.order.domain.vo.Quantity;

import java.util.List;

class OrderCommandMapper {

    private OrderCommandMapper() {
    }

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
