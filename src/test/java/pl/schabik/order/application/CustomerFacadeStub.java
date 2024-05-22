package pl.schabik.order.application;

import pl.schabik.customer.CustomerFacade;

import java.util.UUID;

class CustomerFacadeStub implements CustomerFacade {

    private final boolean customerExists;

    public CustomerFacadeStub(boolean customerExists) {
        this.customerExists = customerExists;
    }

    @Override
    public boolean existsById(UUID customerId) {
        return customerExists;
    }
}
