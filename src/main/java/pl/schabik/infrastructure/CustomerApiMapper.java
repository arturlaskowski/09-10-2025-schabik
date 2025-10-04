package pl.schabik.infrastructure;

import pl.schabik.infrastructure.dto.CreateCustomerRequest;
import pl.schabik.infrastructure.dto.CustomerResponse;
import pl.schabik.usecase.createcustomer.CreateCustomerDto;
import pl.schabik.usecase.getcustomer.CustomerDto;

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