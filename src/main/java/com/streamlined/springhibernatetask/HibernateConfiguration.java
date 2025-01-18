package com.streamlined.springhibernatetask;

import java.util.Map;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@Configuration
public class HibernateConfiguration {

    @Bean(destroyMethod = "close")
    EntityManagerFactory entityManagerFactory(@Value("${USER_NAME}") String userName,
            @Value("${USER_PASSWORD}") String password) {
        Map<?, ?> properties = Map.ofEntries(Map.entry("javax.persistence.jdbc.user", userName),
                Map.entry("javax.persistence.jdbc.password", password));
        return Persistence.createEntityManagerFactory("springhibernatetask", properties);
    }

    @Bean
    Validator validator() {
        try (ValidatorFactory validatorFactory = Validation.byDefaultProvider().configure()
                .messageInterpolator(new ParameterMessageInterpolator()).buildValidatorFactory()) {
            return validatorFactory.getValidator();
        }
    }

}
