package pl.schabik.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.schabik.domain.Customer;
import pl.schabik.domain.CustomerRepository;

import java.util.UUID;

public interface SqlCustomerRepository extends CustomerRepository, JpaRepository<Customer, UUID> {
}