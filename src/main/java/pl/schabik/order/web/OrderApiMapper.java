package pl.schabik.order.web;

import pl.schabik.order.command.dto.CreateOrderAddressDto;
import pl.schabik.order.command.dto.CreateOrderCommand;
import pl.schabik.order.command.dto.CreateOrderItemDto;
import pl.schabik.order.web.dto.CreateOrderRequest;

class OrderApiMapper {

    private OrderApiMapper() {
    }

    public static CreateOrderCommand mapToDto(CreateOrderRequest request) {
        var itemDtos = request.items().stream()
                .map(OrderApiMapper::mapItemToCreateDto)
                .toList();

        var addressDto = mapAddressToDto(request.address());

        return new CreateOrderCommand(
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
