package pl.schabik.infrastructure;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.schabik.infrastructure.dto.CreateCustomerRequest;
import pl.schabik.infrastructure.dto.CustomerResponse;
import pl.schabik.usecase.createcustomer.CreateCustomerService;
import pl.schabik.usecase.getcustomer.GetCustomerService;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CreateCustomerService createCustomerService;
    private final GetCustomerService getCustomerService;

    public CustomerController(CreateCustomerService createCustomerService, GetCustomerService getCustomerService) {
        this.createCustomerService = createCustomerService;
        this.getCustomerService = getCustomerService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable UUID id) {
        var customer = getCustomerService.getCustomer(id);
        var customerResponse = CustomerApiMapper.mapToCustomerResponse(customer);
        return ResponseEntity.ok(customerResponse);
    }

    @PostMapping
    public ResponseEntity<Void> addCustomer(@RequestBody @Valid CreateCustomerRequest createCustomerRequest) {
        var createCustomerDto = CustomerApiMapper.mapToCreateCustomerDto(createCustomerRequest);
        var customerId = createCustomerService.addCustomer(createCustomerDto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(customerId)
                .toUri();

        return ResponseEntity.created(location).build();
    }
}


