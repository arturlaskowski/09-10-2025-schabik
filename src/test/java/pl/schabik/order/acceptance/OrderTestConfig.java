package pl.schabik.order.acceptance;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import pl.schabik.order.CustomerFacadeStub;

@TestConfiguration
class OrderTestConfig {

    @Bean
    @Primary
    public CustomerFacadeStub customerFacade() {
        return new CustomerFacadeStub();
    }
}