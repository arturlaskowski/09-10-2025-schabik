package pl.schabik.order.application.replication;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.schabik.common.CustomerCreatedEvent;

import java.util.UUID;

@Service
public class CustomerProjectionService {

    private final CustomerProjectionRepository customerProjectionRepository;

    public CustomerProjectionService(CustomerProjectionRepository customerProjectionRepository) {
        this.customerProjectionRepository = customerProjectionRepository;
    }

    @EventListener
    public void replicateCustomer(CustomerCreatedEvent customerCreatedEvent) {
        customerProjectionRepository.save(new CustomerProjection(customerCreatedEvent.id()));
    }

    @Transactional(readOnly = true)
    public boolean existsById(UUID customerId) {
        return customerProjectionRepository.existsById(customerId);
    }
}