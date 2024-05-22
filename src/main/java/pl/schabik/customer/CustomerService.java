package pl.schabik.customer;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerService implements CustomerFacade {

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


    @Override
    public boolean existsById(UUID customerId) {
        return customerRepository.existsById(customerId);
    }
}
