package antifraud.configuration;


import antifraud.persitence.models.limits.amountParameters;
import antifraud.persitence.models.limits.builder;
import antifraud.persitence.models.limits.director;
import antifraud.persitence.repos.transaction.amountParametersRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BinaryOperator;

@Configuration
public class configuration {

    @Bean
    public amountParameters getAmountParameters(@Value("${antifraud.manualProcessingAmount}") long manualProcessingAmount,
                                                @Value("${antifraud.prohibitedAmount}") long prohibitedAmount,
                                                @Autowired amountParametersRepo aPR) {



        BinaryOperator<Long> increase = (current_limit, value_from_transaction)
                -> (long) Math.ceil(0.8 * current_limit + 0.2 * value_from_transaction);

        BinaryOperator<Long> decrease = (current_limit, value_from_transaction)
                -> (long) Math.ceil(0.8 * current_limit - 0.2 * value_from_transaction);


        builder builder = new amountParameters.builder();
        director.defaultAmountParameters(builder);

        return builder
                .setProhibitedAmount(prohibitedAmount)
                .setManualProcessingAmount(manualProcessingAmount)
                .setIncreaseFunc(increase)
                .setDecreaseFunc(decrease)
                .build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", new BCryptPasswordEncoder(10));
        encoders.put("noop", NoOpPasswordEncoder.getInstance());
        encoders.put(null, NoOpPasswordEncoder.getInstance());

        return new DelegatingPasswordEncoder("noop", encoders);

    }
}
