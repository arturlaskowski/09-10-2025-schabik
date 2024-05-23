package pl.schabik.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

record CreateCustomerDto(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @Email @NotBlank String email
) {
}

