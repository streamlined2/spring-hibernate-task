package com.streamlined.springhibernatetask.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
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
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-it.properties")
@ContextConfiguration(classes = SpringHibernateTaskApplication.class)
class TrainerRepositoryImplTest {

    @Autowired
    private EntityManagerStorage entityManagerStorage;
    @Autowired
    private TraineeRepositoryImpl traineeRepository;
    @Autowired
    private TrainerRepositoryImpl trainerRepository;
    @Autowired
    private TrainingRepositoryImpl trainingRepository;

    @Test
    void findAllShouldReturnEmptyList_ifTrainerTableContainsNoData() {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerStorage.getEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            deleteAll(entityManager);

            assertEquals(0L, Utilities.stream(trainerRepository.findAll()).count());
        } catch (Exception e) {
            fail("Exception executing test: ", e);
        } finally {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
        }
    }

    private int deleteAll(EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaDelete<Trainer> criteria = cb.createCriteriaDelete(Trainer.class);
        criteria.from(Trainer.class);
        Query query = entityManager.createQuery(criteria);
        return query.executeUpdate();
    }

    @Test
    void findAllShouldReturnListOfAllAvailableTrainerEntities_ifSucceeds() {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerStorage.getEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            List<Trainer> expectedEntityList = List.of(
                    Trainer.builder().firstName("John").lastName("Smith").userName("John.Smith").passwordHash("john")
                            .isActive(true).specialization(TrainingType.builder().id(1L).build()).build(),
                    Trainer.builder().firstName("Jack").lastName("Powell").userName("Jack.Powell").passwordHash("jack")
                            .isActive(true).specialization(TrainingType.builder().id(3L).build()).build(),
                    Trainer.builder().firstName("Robert").lastName("Orwell").userName("Robert.Orwell")
                            .passwordHash("robert").isActive(false)
                            .specialization(TrainingType.builder().id(5L).build()).build());
            Set<String> expectedEntityKeys = expectedEntityList.stream().map(this::getTrainerKey)
                    .collect(Collectors.toSet());

            deleteAll(entityManager);
            saveAll(expectedEntityList);

            Set<String> actualEntityKeys = Utilities.stream(trainerRepository.findAll()).map(this::getTrainerKey)
                    .collect(Collectors.toSet());

            assertEquals(expectedEntityKeys, actualEntityKeys);
        } catch (Exception e) {
            fail("Exception executing test: ", e);
        } finally {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
        }
    }

    private void saveAll(Iterable<Trainer> trainers) {
        for (Trainer trainer : trainers) {
            trainerRepository.create(trainer);
        }
    }

    private String getTrainerKey(Trainer a) {
        return "%s%s%s%s%b%d".formatted(a.getFirstName(), a.getLastName(), a.getUserName(), a.getPasswordHash(),
                a.isActive(), a.getSpecialization().getId());
    }

    @Test
    void findByUserNameShouldReturnEmptyOptional_ifPassedUserNameDoesNotExist() {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerStorage.getEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            deleteAll(entityManager);

            String nonExistingUserName = "John.Smith";
            Optional<Trainer> trainer = trainerRepository.findByUserName(nonExistingUserName);

            assertTrue(trainer.isEmpty());
        } catch (Exception e) {
            fail("Exception executing test: ", e);
        } finally {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
        }
    }

    @Test
    void findByUserNameShouldReturnFoundTrainer_ifPassedUserNameExists() {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerStorage.getEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            deleteAll(entityManager);
            String existingUserName = "John.Smith";
            Trainer trainer = Trainer.builder().firstName("John").lastName("Smith").userName(existingUserName)
                    .passwordHash("john").isActive(true).specialization(TrainingType.builder().id(7L).build()).build();
            trainer = trainerRepository.create(trainer);

            Optional<Trainer> foundTrainer = trainerRepository.findByUserName(existingUserName);

            assertTrue(foundTrainer.isPresent());
            assertEquals(getTrainerKey(trainer), getTrainerKey(foundTrainer.get()));
        } catch (Exception e) {
            fail("Exception executing test: ", e);
        } finally {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
        }
    }

    @Test
    void getTrainingListByUserNameDateRangeTraineeNameType_shouldReturnListOfTraining_ifSucceeds() {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerStorage.getEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            deleteAll(entityManager);
            TrainingType trainingType = TrainingType.builder().id(9L).name("Chemistry").build();
            String traineeName = "John.Smith";
            Trainee trainee = Trainee.builder().firstName("John").lastName("Smith").userName(traineeName)
                    .passwordHash("john").isActive(true).dateOfBirth(LocalDate.of(1990, 1, 1)).address("USA").build();
            trainee = traineeRepository.create(trainee);
            String trainerName = "Richard.Fitz";
            Trainer trainer = Trainer.builder().firstName("Richard").lastName("Fitz").userName(trainerName)
                    .passwordHash("john").isActive(true).specialization(trainingType).build();
            trainer = trainerRepository.create(trainer);
            Training training = Training.builder().trainee(trainee).trainer(trainer).name("Chemistry")
                    .type(trainingType).date(LocalDate.of(2024, 1, 1)).duration(Duration.of(1L, ChronoUnit.DAYS))
                    .build();
            training = trainingRepository.create(training);

            Set<String> trainingKeys = Utilities
                    .stream(trainerRepository.getTrainingListByUserNameDateRangeTraineeNameType(trainerName,
                            LocalDate.of(2023, 1, 1), LocalDate.of(2025, 1, 1), traineeName))
                    .map(this::getTrainingKey).collect(Collectors.toSet());

            assertEquals(1, trainingKeys.size());
            assertEquals(getTrainingKey(training), trainingKeys.iterator().next());
        } catch (Exception e) {
            fail("Exception executing test: ", e);
        } finally {
            if (transaction != null && transaction.isActive())
                transaction.setRollbackOnly();
        }
    }

    private String getTrainingKey(Training a) {
        return "%d%d%d%s%d%tF%s".formatted(a.getId(), a.getTrainee().getId(), a.getTrainer().getId(), a.getName(),
                a.getType().getId(), a.getDate(), a.getDuration().toString());
    }

}
