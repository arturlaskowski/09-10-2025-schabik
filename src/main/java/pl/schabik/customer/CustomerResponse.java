package pl.schabik.customer;

import java.util.UUID;

record CustomerResponse(UUID id, String firstName, String lastName, String email) {
}
