package pl.schabik.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.schabik.service.CustomerService;

import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable UUID id) {
        var customer = customerService.getCustomer(id);
        return ResponseEntity.ok(customer);
    }

    @PostMapping
    public UUID addCustomer(@RequestBody @Valid CreateCustomerDto customerDto) {
        return customerService.addCustomer(customerDto);
    }
}

