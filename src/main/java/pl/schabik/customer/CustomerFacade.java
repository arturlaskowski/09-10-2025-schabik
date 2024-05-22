package pl.schabik.customer;

import java.util.UUID;

public interface CustomerFacade {

    boolean existsById(UUID customerId);
}
