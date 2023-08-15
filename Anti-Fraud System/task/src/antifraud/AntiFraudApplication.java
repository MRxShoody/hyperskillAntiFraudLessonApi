package antifraud;

import antifraud.persitence.models.limits.amountParameters;
import antifraud.persitence.models.limits.builder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AntiFraudApplication {

    public static void main(String[] args) {

          builder amountParametersBuilder = new amountParameters.builder();
        SpringApplication.run(AntiFraudApplication.class, args);
    }


}