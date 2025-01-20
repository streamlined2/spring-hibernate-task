package com.streamlined.springhibernatetask.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.streamlined.springhibernatetask.EntityManagerStorage;
import com.streamlined.springhibernatetask.SpringHibernateTaskApplication;
import com.streamlined.springhibernatetask.Utilities;
import com.streamlined.springhibernatetask.entity.Trainee;
import com.streamlined.springhibernatetask.entity.Trainer;
import com.streamlined.springhibernatetask.entity.Training;
import com.streamlined.springhibernatetask.entity.TrainingType;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-it.properties")
@ContextConfiguration(classes = SpringHibernateTaskApplication.class)
class TrainingRepositoryImplTest {

    @Autowired
    private EntityManagerStorage entityManagerStorage;
    @Autowired
    private TraineeRepositoryImpl traineeRepository;
    @Autowired
    private TrainerRepositoryImpl trainerRepository;
    @Autowired
    private TrainingRepositoryImpl trainingRepository;

    @Test
    void findAllShouldReturnEmptyList_ifTrainingTableContainsNoData() {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerStorage.getEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            deleteAll(Training.class, entityManager);

            assertEquals(0L, Utilities.stream(trainingRepository.findAll()).count());
        } catch (Exception e) {
            fail("Exception executing test: ", e);
        } finally {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
        }
    }

    private <T> int deleteAll(Class<T> entityClass, EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaDelete<T> criteria = cb.createCriteriaDelete(entityClass);
        criteria.from(entityClass);
        return entityManager.createQuery(criteria).executeUpdate();
    }

    @Test
    void findAllShouldReturnListOfAllAvailableTrainingEntities_ifSucceeds() {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerStorage.getEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            deleteAll(Trainee.class, entityManager);
            deleteAll(Trainer.class, entityManager);
            deleteAll(Training.class, entityManager);

            TrainingType trainingType = TrainingType.builder().id(1L).build();
            Trainer trainer = trainerRepository.create(Trainer.builder().firstName("John").lastName("Smith")
                    .userName("John.Smith").passwordHash("john").isActive(true).specialization(trainingType).build());
            String traineeName = "Ken.Harmful";
            Trainee trainee = traineeRepository.create(
                    Trainee.builder().firstName("Ken").lastName("Harmful").userName(traineeName).passwordHash("john")
                            .isActive(true).dateOfBirth(LocalDate.of(1990, 1, 1)).address("USA").build());

            Training training1 = trainingRepository
                    .create(Training.builder().trainee(trainee).trainer(trainer).name("Art").type(trainingType)
                            .date(LocalDate.of(2024, 1, 1)).duration(Duration.of(1L, ChronoUnit.DAYS)).build());
            Training training2 = trainingRepository
                    .create(Training.builder().trainee(trainee).trainer(trainer).name("Geography").type(trainingType)
                            .date(LocalDate.of(2024, 2, 1)).duration(Duration.of(1L, ChronoUnit.DAYS)).build());
            Training training3 = trainingRepository
                    .create(Training.builder().trainee(trainee).trainer(trainer).name("Computers").type(trainingType)
                            .date(LocalDate.of(2024, 3, 1)).duration(Duration.of(1L, ChronoUnit.DAYS)).build());
            List<Training> expectedEntityList = List.of(training1, training2, training3);
            Set<String> expectedEntityKeys = expectedEntityList.stream().map(this::getTrainingKey)
                    .collect(Collectors.toSet());

            Set<String> actualEntityKeys = Utilities.stream(trainingRepository.findAll()).map(this::getTrainingKey)
                    .collect(Collectors.toSet());

            assertEquals(expectedEntityKeys, actualEntityKeys);
        } catch (Exception e) {
            fail("Exception executing test: ", e);
        } finally {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
        }
    }

    private void saveAll(Iterable<Training> trainings) {
        for (Training training : trainings) {
            trainingRepository.create(training);
        }
    }

    private String getTrainingKey(Training a) {
        return "%d%d%d%s%d%tF%s".formatted(a.getId(), a.getTrainee().getId(), a.getTrainee().getId(), a.getName(),
                a.getType().getId(), a.getDate(), a.getDuration().toString());
    }

}
