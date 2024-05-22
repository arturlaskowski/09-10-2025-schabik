package pl.schabik.order.infrastructure;

import pl.schabik.order.application.dto.CreateOrderAddressDto;
import pl.schabik.order.application.dto.CreateOrderDto;
import pl.schabik.order.application.dto.CreateOrderItemDto;

import java.util.stream.Collectors;

public class OrderApiMapper {

    public static CreateOrderDto mapToDto(CreateOrderRequest request) {
        var itemDtos = request.items().stream()
                .map(OrderApiMapper::mapItemToCreateDto)
                .collect(Collectors.toList());

        var addressDto = mapAddressToDto(request.address());

        return new CreateOrderDto(
                request.customerId(),
                request.price(),
                itemDtos,
                addressDto
        );
    }

    private static CreateOrderItemDto mapItemToCreateDto(CreateOrderRequest.OrderItemRequest item) {
        return new CreateOrderItemDto(
                item.productId(),
                item.quantity(),
                item.price(),
                item.totalPrice()
        );
    }

    private static CreateOrderAddressDto mapAddressToDto(CreateOrderRequest.OrderAddressRequest address) {
        return new CreateOrderAddressDto(
                address.street(),
                address.postalCode(),
                address.city(),
                address.houseNo()
        );
    }
}
