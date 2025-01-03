package pl.schabik.order.query.dto;

public record OrderAddressDto(
        String street,
        String postalCode,
        String city,
        String houseNo) {
}