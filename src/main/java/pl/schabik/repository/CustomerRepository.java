package pl.schabik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.schabik.model.Customer;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    boolean existsByEmail(String email);
}
