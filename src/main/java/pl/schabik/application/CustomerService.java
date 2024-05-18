package pl.schabik.application;

import org.springframework.stereotype.Service;
import pl.schabik.application.dto.CreateCustomerDto;
import pl.schabik.application.dto.CustomerDto;
import pl.schabik.application.exception.CustomerAlreadyExistsException;
import pl.schabik.application.exception.CustomerNotFoundException;
import pl.schabik.domain.Customer;
import pl.schabik.domain.CustomerRepository;

import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerDto getCustomer(UUID id) {
        return customerRepository.findById(id)
                .map(customer -> new CustomerDto(id, customer.getFirstName(), customer.getLastName(), customer.getEmail()))
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    public UUID addCustomer(CreateCustomerDto customerDto) {
        if (customerRepository.existsByEmail(customerDto.email())) {
            throw new CustomerAlreadyExistsException(customerDto.email());
        }
        var customer = new Customer(customerDto.firstName(), customerDto.lastName(), customerDto.email());
        return customerRepository.save(customer).getId();
    }
}