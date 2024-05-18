package pl.schabik.infrastructure;

import pl.schabik.application.dto.CreateOrderAddressDto;
import pl.schabik.application.dto.CreateOrderDto;
import pl.schabik.application.dto.CreateOrderItemDto;

public class OrderApiMapper {

    public static CreateOrderDto mapToDto(CreateOrderRequest request) {
        var itemDtos = request.items().stream()
                .map(OrderApiMapper::mapItemToCreateDto)
                .toList();

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