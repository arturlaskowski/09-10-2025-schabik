package pl.schabik.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface SqlCustomerRepository extends CustomerRepository, JpaRepository<Customer, UUID> {
}