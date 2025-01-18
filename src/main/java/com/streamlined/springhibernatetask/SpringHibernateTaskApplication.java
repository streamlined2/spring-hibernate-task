package com.streamlined.springhibernatetask;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.streamlined.springhibernatetask.repository.UserRepository;
import com.streamlined.springhibernatetask.service.SecurityService;
import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class SpringHibernateTaskApplication implements CommandLineRunner {

    private final UserRepository userRepository;
    private final SecurityService securityService;

    public static void main(String[] args) {
        SpringApplication.run(SpringHibernateTaskApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        userRepository.findAll().forEach(System.out::println);

        System.out.println(userRepository.existsById(1L));
        System.out.println(userRepository.existsById(100L));

        System.out.println(userRepository.findById(2L));
        System.out.println(userRepository.findById(200L));

        System.out.println(userRepository.count());

        List<Long> ids = List.of(5L, 6L, 7L, 8L, 9L);
        System.out.println(userRepository.findAllById(ids));

        System.out.println(userRepository.getMaxUsername("Robert", "Orwell"));

        System.out.println(securityService.getPasswordHash("password".toCharArray()));
    }

    @Bean
    CsvMapper csvMapper() {
        CsvMapper csvMapper = new CsvMapper();
        csvMapper.registerModule(new JavaTimeModule());
        return csvMapper;
    }

}
