package pl.schabik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.schabik.model.Customer;

import java.util.UUID;

public interface SqlCustomerRepository extends CustomerRepository, JpaRepository<Customer, UUID> {
}