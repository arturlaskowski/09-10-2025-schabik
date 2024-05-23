package pl.schabik.customer;

import java.util.UUID;

record CustomerDto(UUID id, String firstName, String lastName, String email) {
}
