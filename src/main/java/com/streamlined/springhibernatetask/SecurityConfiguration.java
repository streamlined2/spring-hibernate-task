package com.streamlined.springhibernatetask;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.PasswordEncoder;

import exception.MissingAlgorithmException;

@Configuration
public class SecurityConfiguration {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(BCryptVersion.$2Y);
    }

    @Bean
    Random random(@Value("${algorithm.random}") String algorithmName) {
        try {
            return SecureRandom.getInstance(algorithmName);
        } catch (NoSuchAlgorithmException e) {
            throw new MissingAlgorithmException("Missing random generator algorithm", e);
        }
    }

}
