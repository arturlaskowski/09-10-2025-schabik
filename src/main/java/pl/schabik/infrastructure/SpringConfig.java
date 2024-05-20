package pl.schabik.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.schabik.domain.CustomerRepository;
import pl.schabik.domain.OrderRepository;
import pl.schabik.usecase.createcustomer.CreateCustomerService;
import pl.schabik.usecase.createorder.CreateOrderService;
import pl.schabik.usecase.getcustomer.GetCustomerService;
import pl.schabik.usecase.getorder.GetOrderService;
import pl.schabik.usecase.payorder.PayOrderService;

@Configuration
public class SpringConfig {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    public SpringConfig(CustomerRepository customerRepository, OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    @Bean
    public CreateOrderService createOrderService() {
        return new CreateOrderService(orderRepository, customerRepository);
    }

    @Bean
    public PayOrderService payOrderService() {
        return new PayOrderService(orderRepository);
    }

    @Bean
    public GetOrderService getOrderService() {
        return new GetOrderService(orderRepository);
    }

    @Bean
    public CreateCustomerService createCustomerService() {
        return new CreateCustomerService(customerRepository);
    }

    @Bean
    public GetCustomerService getCustomerService() {
        return new GetCustomerService(customerRepository);
    }
}
