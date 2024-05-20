package pl.schabik.usecase.getorder;

public record OrderAddressDto(
        String street,
        String postalCode,
        String city,
        String houseNo) {
}
