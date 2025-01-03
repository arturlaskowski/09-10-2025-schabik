package pl.schabik.order.web;

import pl.schabik.order.command.create.CreateOrderAddressDto;
import pl.schabik.order.command.create.CreateOrderCommand;
import pl.schabik.order.command.create.CreateOrderItemDto;
import pl.schabik.order.domain.vo.OrderId;
import pl.schabik.order.web.dto.CreateOrderRequest;

class OrderApiMapper {

    private OrderApiMapper() {
    }

    public static CreateOrderCommand mapToOrderCommand(OrderId orderId, CreateOrderRequest request) {
        var itemDtos = request.items().stream()
                .map(OrderApiMapper::mapItemToCreateDto)
                .toList();

        var addressDto = mapAddressToDto(request.address());

        return new CreateOrderCommand(
                orderId,
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
