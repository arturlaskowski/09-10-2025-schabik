package pl.schabik.customer;

class CustomerApiMapper {

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
