package pl.schabik.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateOrderAddressDto(
        @NotBlank String street,
        @NotBlank String postalCode,
        @NotBlank String city,
        @NotBlank String houseNo) {
}