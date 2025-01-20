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

            deleteAll(Trainer.class, entityManager);

            assertEquals(0L, Utilities.stream(trainerRepository.findAll()).count());
        } catch (Exception e) {
            fail("Exception executing test: ", e);
        } finally {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
        }
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

            deleteAll(Trainer.class, entityManager);
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

            deleteAll(Trainer.class, entityManager);

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

            deleteAll(Trainer.class, entityManager);
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

            deleteAll(Trainee.class, entityManager);
            deleteAll(Trainer.class, entityManager);
            deleteAll(Training.class, entityManager);
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

    @Test
    void getNonAssignedTrainers_shouldReturnListOfTraining_ifSucceeds() {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerStorage.getEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            deleteAll(Trainee.class, entityManager);
            deleteAll(Trainer.class, entityManager);
            deleteAll(Training.class, entityManager);

            TrainingType trainingType1 = TrainingType.builder().id(1L).build();
            Trainer trainer1 = Trainer.builder().firstName("John").lastName("Smith").userName("John.Smith")
                    .passwordHash("john").isActive(true).specialization(trainingType1).build();
            TrainingType trainingType2 = TrainingType.builder().id(3L).build();
            Trainer trainer2 = Trainer.builder().firstName("Jack").lastName("Powell").userName("Jack.Powell")
                    .passwordHash("jack").isActive(true).specialization(trainingType2).build();
            TrainingType trainingType3 = TrainingType.builder().id(5L).build();
            Trainer trainer3 = Trainer.builder().firstName("Robert").lastName("Orwell").userName("Robert.Orwell")
                    .passwordHash("robert").isActive(false).specialization(trainingType3).build();
            List<Trainer> trainerList = List.of(trainer1, trainer2, trainer3);
            saveAll(trainerList);

            String traineeName = "Ken.Harmful";
            Trainee trainee = Trainee.builder().firstName("Ken").lastName("Harmful").userName(traineeName)
                    .passwordHash("john").isActive(true).dateOfBirth(LocalDate.of(1990, 1, 1)).address("USA").build();
            trainee = traineeRepository.create(trainee);
            String otherTraineeName = "Jenny.Welsh";
            Trainee otherTrainee = Trainee.builder().firstName("Jenny").lastName("Welsh").userName(otherTraineeName)
                    .passwordHash("jenny").isActive(true).dateOfBirth(LocalDate.of(1996, 1, 1)).address("Canada")
                    .build();
            otherTrainee = traineeRepository.create(otherTrainee);

            Training training1 = Training.builder().trainee(otherTrainee).trainer(trainer1).name("Art")
                    .type(trainingType1).date(LocalDate.of(2024, 1, 1)).duration(Duration.of(1L, ChronoUnit.DAYS))
                    .build();
            training1 = trainingRepository.create(training1);
            Training training2 = Training.builder().trainee(trainee).trainer(trainer2).name("Geography")
                    .type(trainingType2).date(LocalDate.of(2024, 2, 1)).duration(Duration.of(1L, ChronoUnit.DAYS))
                    .build();
            training2 = trainingRepository.create(training2);
            Training training3 = Training.builder().trainee(trainee).trainer(trainer3).name("Computers")
                    .type(trainingType3).date(LocalDate.of(2024, 3, 1)).duration(Duration.of(1L, ChronoUnit.DAYS))
                    .build();
            training3 = trainingRepository.create(training3);

            Set<String> trainerKeys = Utilities.stream(trainerRepository.getNonAssignedTrainers(traineeName))
                    .map(this::getTrainerKey).collect(Collectors.toSet());

            assertEquals(1, trainerKeys.size());
            assertEquals(getTrainerKey(trainer1), trainerKeys.iterator().next());
        } catch (Exception e) {
            fail("Exception executing test: ", e);
        } finally {
            if (transaction != null && transaction.isActive())
                transaction.setRollbackOnly();
        }
    }

    private <T> int deleteAll(Class<T> entityClass, EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaDelete<T> criteria = cb.createCriteriaDelete(entityClass);
        criteria.from(entityClass);
        return entityManager.createQuery(criteria).executeUpdate();
    }

    @Test
    void findByIdShouldReturnEmptyTrainerEntity_ifEntityWithPassedIdDoesNotExist() {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerStorage.getEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            deleteAll(Trainer.class, entityManager);

            Long nonExistingId = -1L;
            Optional<Trainer> trainer = trainerRepository.findById(nonExistingId);

            assertTrue(trainer.isEmpty());
        } catch (Exception e) {
            fail("Exception executing test: ", e);
        } finally {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
        }
    }

    @Test
    void findByIdShouldReturnFoundTrainerEntity_ifEntityWithPassedIdExists() {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerStorage.getEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            deleteAll(Trainer.class, entityManager);

            TrainingType trainingType = TrainingType.builder().id(3L).build();
            Trainer trainer = Trainer.builder().firstName("Jack").lastName("Powell").userName("Jack.Powell")
                    .passwordHash("jack").isActive(true).specialization(trainingType).build();
            trainer = trainerRepository.create(trainer);

            Optional<Trainer> foundTrainer = trainerRepository.findById(trainer.getId());

            assertTrue(foundTrainer.isPresent());
            assertEquals(getTrainerKey(trainer), getTrainerKey(foundTrainer.get()));
        } catch (Exception e) {
            fail("Exception executing test: ", e);
        } finally {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
        }
    }

}
