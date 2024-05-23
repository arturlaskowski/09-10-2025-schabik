package pl.schabik.customer;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pl.schabik.common.CustomerCreatedEvent;

import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public CustomerService(CustomerRepository customerRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.customerRepository = customerRepository;
        this.applicationEventPublisher = applicationEventPublisher;
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
        var customerId = customerRepository.save(customer).getId();
        applicationEventPublisher.publishEvent(new CustomerCreatedEvent(customerId));
        return customerId;
    }
}
