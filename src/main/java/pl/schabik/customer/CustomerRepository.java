package pl.schabik.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface CustomerRepository extends JpaRepository<Customer, UUID> {

    boolean existsByEmail(String email);
}