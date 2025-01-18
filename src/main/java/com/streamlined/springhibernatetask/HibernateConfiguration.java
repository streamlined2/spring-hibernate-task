package com.streamlined.springhibernatetask;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
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
    EntityManagerFactory entityManagerFactory() {
        return Persistence.createEntityManagerFactory("springhibernatetask");
    }

    @Bean
    Validator validator() {
        try (ValidatorFactory validatorFactory = Validation.byDefaultProvider().configure()
                .messageInterpolator(new ParameterMessageInterpolator()).buildValidatorFactory()) {
            return validatorFactory.getValidator();
        }
    }

}
