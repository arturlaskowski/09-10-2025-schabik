package pl.schabik.infrastructure;

import pl.schabik.infrastructure.dto.CreateOrderRequest;
import pl.schabik.infrastructure.dto.GetOrderAddressResponse;
import pl.schabik.infrastructure.dto.GetOrderItemResponse;
import pl.schabik.infrastructure.dto.GetOrderResponse;
import pl.schabik.usecase.createorder.CreateOrderAddressDto;
import pl.schabik.usecase.createorder.CreateOrderDto;
import pl.schabik.usecase.createorder.CreateOrderItemDto;
import pl.schabik.usecase.getorder.OrderAddressDto;
import pl.schabik.usecase.getorder.OrderDto;
import pl.schabik.usecase.getorder.OrderItemDto;

public class OrderApiMapper {

    private OrderApiMapper() {
    }

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

    public static GetOrderResponse mapToGetOrderResponse(OrderDto dto) {
        var itemsResponse = dto.items().stream()
                .map(OrderApiMapper::mapToItemResponse)
                .toList();

        var addressDto = mapToAddressResponse(dto.address());

        return new GetOrderResponse(
                dto.id(),
                dto.customerId(),
                dto.price(),
                dto.status().name(),
                itemsResponse,
                addressDto
        );
    }

    private static GetOrderItemResponse mapToItemResponse(OrderItemDto dto) {
        return new GetOrderItemResponse(
                dto.productId(),
                dto.quantity(),
                dto.price(),
                dto.totalPrice()
        );
    }

    private static GetOrderAddressResponse mapToAddressResponse(OrderAddressDto dto) {
        return new GetOrderAddressResponse(
                dto.street(),
                dto.postalCode(),
                dto.city(),
                dto.houseNo()
        );
    }
}