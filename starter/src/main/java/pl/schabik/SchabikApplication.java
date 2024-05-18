package pl.schabik;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "pl.schabik")
public class SchabikApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchabikApplication.class, args);
    }

}
