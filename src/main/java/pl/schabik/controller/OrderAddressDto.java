package pl.schabik.controller;

import jakarta.validation.constraints.NotBlank;


public record OrderAddressDto(
        @NotBlank String street,
        @NotBlank String postalCode,
        @NotBlank String city,
        @NotBlank String houseNo) {
}
