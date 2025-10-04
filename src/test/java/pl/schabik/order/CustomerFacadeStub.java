package pl.schabik.order;

import pl.schabik.customer.CustomerFacade;

import java.util.UUID;

public class CustomerFacadeStub implements CustomerFacade {

    private boolean customerExists = true;

    @Override
    public boolean existsById(UUID customerId) {
        return customerExists;
    }

    public void setCustomerExists(boolean customerExists) {
        this.customerExists = customerExists;
    }
}
