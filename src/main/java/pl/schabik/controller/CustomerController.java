package pl.schabik.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.schabik.service.CustomerService;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable UUID id) {
        var customerDto = customerService.getCustomer(id);
        return ResponseEntity.ok(customerDto);
    }

    @PostMapping
    public ResponseEntity<Void> addCustomer(@RequestBody @Valid CreateCustomerDto createCustomerDto) {
        var customerId = customerService.addCustomer(createCustomerDto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(customerId)
                .toUri();

        return ResponseEntity.created(location).build();
    }
}

