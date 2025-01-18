package com.streamlined.springhibernatetask;

import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.streamlined.springhibernatetask.repository.TrainingTypeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

@Configuration
@ComponentScan("com.streamlined.springhibernatetask")
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
public class SpringHibernateTaskApplication {

    public static void main(String[] args) {
        new SpringHibernateTaskApplication().run();
    }

    void run() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                SpringHibernateTaskApplication.class);
                EntityManagerFactory entityManagerFactory = context.getBean(EntityManagerFactory.class);
                EntityManager entityManager = entityManagerFactory.createEntityManager()) {

            TrainingTypeRepository trainingRepository = context.getBean(TrainingTypeRepository.class);

            trainingRepository.findAll().forEach(System.out::println);

            System.out.println(trainingRepository.findById(2L));
            System.out.println(trainingRepository.findById(200L));

            List<Long> ids = List.of(5L, 6L, 7L, 8L, 9L);
            System.out.println(trainingRepository.findAllById(ids));
        }
    }

}
