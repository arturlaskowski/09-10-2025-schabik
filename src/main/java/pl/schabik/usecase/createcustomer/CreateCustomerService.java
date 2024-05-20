package pl.schabik.usecase.createcustomer;

import pl.schabik.domain.Customer;
import pl.schabik.domain.CustomerRepository;

import java.util.UUID;

public class CreateCustomerService {

    private final CustomerRepository customerRepository;

    public CreateCustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public UUID addCustomer(CreateCustomerDto customerDto) {
        if (customerRepository.existsByEmail(customerDto.email())) {
            throw new CustomerAlreadyExistsException(customerDto.email());
        }
        var customer = new Customer(customerDto.firstName(), customerDto.lastName(), customerDto.email());
        return customerRepository.save(customer).getId();
    }
}
