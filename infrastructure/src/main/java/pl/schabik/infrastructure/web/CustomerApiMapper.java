package pl.schabik.infrastructure.web;

import pl.schabik.application.dto.CreateCustomerDto;
import pl.schabik.application.dto.CustomerDto;

public class CustomerApiMapper {

    private CustomerApiMapper() {
    }

    public static CustomerResponse mapToCustomerResponse(CustomerDto customerDto) {
        return new CustomerResponse(
                customerDto.id(),
                customerDto.firstName(),
                customerDto.lastName(),
                customerDto.email());
    }

    public static CreateCustomerDto mapToCreateCustomerDto(CreateCustomerRequest request) {
        return new CreateCustomerDto(
                request.firstName(),
                request.lastName(),
                request.email());
    }
}
