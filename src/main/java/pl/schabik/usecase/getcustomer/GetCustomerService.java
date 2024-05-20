package pl.schabik.usecase.getcustomer;

import pl.schabik.domain.CustomerRepository;

import java.util.UUID;

public class GetCustomerService {

    private final CustomerRepository customerRepository;

    public GetCustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerDto getCustomer(UUID id) {
        return customerRepository.findById(id)
                .map(customer -> new CustomerDto(id, customer.getFirstName(), customer.getLastName(), customer.getEmail()))
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }
}
