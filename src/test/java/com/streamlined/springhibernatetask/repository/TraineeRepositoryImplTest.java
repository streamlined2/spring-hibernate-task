package com.streamlined.springhibernatetask.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.streamlined.springhibernatetask.Utilities;
import com.streamlined.springhibernatetask.EntityManagerStorage;
import com.streamlined.springhibernatetask.SpringHibernateTaskApplication;
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
class TraineeRepositoryImplTest {

    @Autowired
    private EntityManagerStorage entityManagerStorage;
    @Autowired
    private TraineeRepositoryImpl traineeRepository;
    @Autowired
    private TrainerRepositoryImpl trainerRepository;
    @Autowired
    private TrainingRepositoryImpl trainingRepository;

    @Test
    void findAllShouldReturnEmptyList_ifTraineeTableContainsNoData() {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerStorage.getEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            deleteAll(entityManager);

            assertEquals(0L, Utilities.stream(traineeRepository.findAll()).count());
        } catch (Exception e) {
            fail("Exception executing test: ", e);
        } finally {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
        }
    }

    private int deleteAll(EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaDelete<Trainee> criteria = cb.createCriteriaDelete(Trainee.class);
        criteria.from(Trainee.class);
        Query query = entityManager.createQuery(criteria);
        return query.executeUpdate();
    }

    @Test
    void findAllShouldReturnListOfAllAvailableTraineeEntities_ifSucceeds() {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerStorage.getEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            List<Trainee> expectedEntityList = List.of(
                    Trainee.builder().firstName("John").lastName("Smith").userName("John.Smith").passwordHash("john")
                            .isActive(true).dateOfBirth(LocalDate.of(1990, 1, 1)).address("USA").build(),
                    Trainee.builder().firstName("Jack").lastName("Powell").userName("Jack.Powell").passwordHash("jack")
                            .isActive(true).dateOfBirth(LocalDate.of(1990, 2, 15)).address("UK").build(),
                    Trainee.builder().firstName("Robert").lastName("Orwell").userName("Robert.Orwell")
                            .passwordHash("robert").isActive(false).dateOfBirth(LocalDate.of(1991, 3, 10)).address("UK")
                            .build());
            Set<String> expectedEntityKeys = expectedEntityList.stream().map(this::getTraineeKey)
                    .collect(Collectors.toSet());

            deleteAll(entityManager);
            saveAll(expectedEntityList);

            Set<String> actualEntityKeys = Utilities.stream(traineeRepository.findAll()).map(this::getTraineeKey)
                    .collect(Collectors.toSet());

            assertEquals(expectedEntityKeys, actualEntityKeys);
        } catch (Exception e) {
            fail("Exception executing test: ", e);
        } finally {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
        }
    }

    private void saveAll(Iterable<Trainee> trainees) {
        for (Trainee trainee : trainees) {
            traineeRepository.create(trainee);
        }
    }

    private String getTraineeKey(Trainee a) {
        return "%s%s%s%s%b%tF%s".formatted(a.getFirstName(), a.getLastName(), a.getUserName(), a.getPasswordHash(),
                a.isActive(), a.getDateOfBirth(), a.getAddress());
    }

    @Test
    void findByUserNameShouldReturnEmptyOptional_ifPassedUserNameDoesNotExist() {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerStorage.getEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            deleteAll(entityManager);

            String nonExistingUserName = "John.Smith";
            Optional<Trainee> trainee = traineeRepository.findByUserName(nonExistingUserName);

            assertTrue(trainee.isEmpty());
        } catch (Exception e) {
            fail("Exception executing test: ", e);
        } finally {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
        }
    }

    @Test
    void findByUserNameShouldReturnFoundTrainee_ifPassedUserNameExists() {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerStorage.getEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            deleteAll(entityManager);
            String existingUserName = "John.Smith";
            Trainee trainee = Trainee.builder().firstName("John").lastName("Smith").userName(existingUserName)
                    .passwordHash("john").isActive(true).dateOfBirth(LocalDate.of(1990, 1, 1)).address("USA").build();
            trainee = traineeRepository.create(trainee);

            Optional<Trainee> foundTrainee = traineeRepository.findByUserName(existingUserName);

            assertTrue(foundTrainee.isPresent());
            assertEquals(getTraineeKey(trainee), getTraineeKey(foundTrainee.get()));
        } catch (Exception e) {
            fail("Exception executing test: ", e);
        } finally {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
        }
    }

    @Test
    void deleteByUserNameShouldDoNothing_ifPassedUserNameDoesNotExist() {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerStorage.getEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            deleteAll(entityManager);
            String existingUserName = "John.Smith";
            Trainee trainee = Trainee.builder().firstName("John").lastName("Smith").userName(existingUserName)
                    .passwordHash("john").isActive(true).dateOfBirth(LocalDate.of(1990, 1, 1)).address("USA").build();
            trainee = traineeRepository.create(trainee);

            String nonExistingUserName = "Jack.Welsh";
            traineeRepository.deleteByUserName(nonExistingUserName);

            Optional<Trainee> existingTrainee = traineeRepository.findByUserName(existingUserName);
            Optional<Trainee> nonExistingTrainee = traineeRepository.findByUserName(nonExistingUserName);

            assertTrue(nonExistingTrainee.isEmpty());
            assertTrue(existingTrainee.isPresent());
            assertEquals(getTraineeKey(trainee), getTraineeKey(existingTrainee.get()));
        } catch (Exception e) {
            fail("Exception executing test: ", e);
        } finally {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
        }
    }

    @Test
    void deleteByUserNameShouldDeleteFoundEntity_ifPassedUserNameExists() {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerStorage.getEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            deleteAll(entityManager);
            String userName = "John.Smith";
            Trainee trainee = Trainee.builder().firstName("John").lastName("Smith").userName(userName)
                    .passwordHash("john").isActive(true).dateOfBirth(LocalDate.of(1990, 1, 1)).address("USA").build();
            trainee = traineeRepository.create(trainee);

            traineeRepository.deleteByUserName(userName);

            Optional<Trainee> deletedTrainee = traineeRepository.findByUserName(userName);

            assertTrue(deletedTrainee.isEmpty());
        } catch (Exception e) {
            fail("Exception executing test: ", e);
        } finally {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
        }
    }

    @Test
    void findByIdShouldReturnEmptyTraineeEntity_ifEntityWithPassedIdDoesNotExist() {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerStorage.getEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            deleteAll(entityManager);

            Long nonExistingId = -1L;
            Optional<Trainee> trainee = traineeRepository.findById(nonExistingId);

            assertTrue(trainee.isEmpty());
        } catch (Exception e) {
            fail("Exception executing test: ", e);
        } finally {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
        }
    }

    @Test
    void findByIdShouldReturnFoundTraineeEntity_ifEntityWithPassedIdExists() {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerStorage.getEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            deleteAll(entityManager);

            String userName = "John.Smith";
            Trainee trainee = Trainee.builder().firstName("John").lastName("Smith").userName(userName)
                    .passwordHash("john").isActive(true).dateOfBirth(LocalDate.of(1990, 1, 1)).address("USA").build();
            trainee = traineeRepository.create(trainee);

            Optional<Trainee> foundTrainee = traineeRepository.findById(trainee.getId());

            assertTrue(foundTrainee.isPresent());
            assertEquals(getTraineeKey(trainee), getTraineeKey(foundTrainee.get()));
        } catch (Exception e) {
            fail("Exception executing test: ", e);
        } finally {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
        }
    }

    @Test
    void deleteByIdShouldDoNothing_ifEntityWithPassedIdDoesNotExist() {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerStorage.getEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            deleteAll(entityManager);

            Long nonExistingId = -1L;
            traineeRepository.deleteById(nonExistingId);

            assertTrue(isEmptyTable());
        } catch (Exception e) {
            fail("Exception executing test: ", e);
        } finally {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
        }
    }

    @Test
    void deleteByIdShouldDeleteEntityWithPassedId_ifSuchEntityExists() {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerStorage.getEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            deleteAll(entityManager);
            Trainee trainee = Trainee.builder().firstName("John").lastName("Smith").userName("John.Smith")
                    .passwordHash("john").isActive(true).dateOfBirth(LocalDate.of(1990, 1, 1)).address("USA").build();
            trainee = traineeRepository.create(trainee);

            traineeRepository.deleteById(trainee.getId());

            assertTrue(isEmptyTable());
        } catch (Exception e) {
            fail("Exception executing test: ", e);
        } finally {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
        }
    }

    private boolean isEmptyTable() {
        return !traineeRepository.findAll().iterator().hasNext();
    }

    @Test
    void createShouldCreateNewEntity_ifSucceeds() {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerStorage.getEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            deleteAll(entityManager);
            Trainee trainee = Trainee.builder().firstName("John").lastName("Smith").userName("John.Smith")
                    .passwordHash("john").isActive(true).dateOfBirth(LocalDate.of(1990, 1, 1)).address("USA").build();
            trainee = traineeRepository.create(trainee);

            Optional<Trainee> foundTrainee = traineeRepository.findById(trainee.getId());

            assertTrue(foundTrainee.isPresent());
            assertEquals(getTraineeKey(trainee), getTraineeKey(foundTrainee.get()));
        } catch (Exception e) {
            fail("Exception executing test: ", e);
        } finally {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
        }
    }

    @Test
    void createShouldNotCreateNewEntityAndThrowException_ifPassedUserNameAlreadyExists() {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerStorage.getEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            deleteAll(entityManager);
            String userName = "John.Smith";
            Trainee trainee = Trainee.builder().firstName("John").lastName("Smith").userName(userName)
                    .passwordHash("john").isActive(true).dateOfBirth(LocalDate.of(1990, 1, 1)).address("USA").build();
            traineeRepository.create(trainee);

            Trainee newTrainee = Trainee.builder().firstName("Jack").lastName("Robertson").userName(userName)
                    .passwordHash("jack").isActive(true).dateOfBirth(LocalDate.of(1995, 12, 1)).address("USA").build();

            Exception exc = assertThrows(ConstraintViolationException.class,
                    () -> traineeRepository.create(newTrainee));
            assertTrue(exc.getMessage().contains("duplicate key value violates unique constraint"));
        } catch (Exception e) {
            fail("Exception executing test: ", e);
        } finally {
            if (transaction != null && transaction.isActive())
                transaction.setRollbackOnly();
        }
    }

    @Test
    void updateShouldUpdateEntity_ifSuchEntityExists() {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerStorage.getEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            deleteAll(entityManager);
            String userName = "John.Smith";
            Trainee trainee = Trainee.builder().firstName("John").lastName("Smith").userName(userName)
                    .passwordHash("john").isActive(true).dateOfBirth(LocalDate.of(1990, 1, 1)).address("USA").build();
            trainee = traineeRepository.create(trainee);

            trainee.setDateOfBirth(LocalDate.of(1991, 2, 1));
            trainee.setAddress("Canada");
            trainee.setPasswordHash("smith");
            traineeRepository.update(trainee);

            Optional<Trainee> foundTrainee = traineeRepository.findById(trainee.getId());

            assertTrue(foundTrainee.isPresent());
            assertEquals(trainee.getFirstName(), foundTrainee.get().getFirstName());
            assertEquals(trainee.getLastName(), foundTrainee.get().getLastName());
            assertEquals(trainee.getUserName(), foundTrainee.get().getUserName());
            assertEquals(trainee.getPasswordHash(), foundTrainee.get().getPasswordHash());
            assertEquals(trainee.isActive(), foundTrainee.get().isActive());
            assertEquals(trainee.getDateOfBirth(), foundTrainee.get().getDateOfBirth());
            assertEquals(trainee.getAddress(), foundTrainee.get().getAddress());
        } catch (Exception e) {
            fail("Exception executing test: ", e);
        } finally {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
        }
    }

    @Test
    void updateShouldCreateEntity_ifSuchEntityDoesNotExist() {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerStorage.getEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            deleteAll(entityManager);
            String userName = "John.Smith";
            Trainee trainee = Trainee.builder().firstName("John").lastName("Smith").userName(userName)
                    .passwordHash("john").isActive(true).dateOfBirth(LocalDate.of(1990, 1, 1)).address("USA").build();

            trainee = traineeRepository.update(trainee);

            Optional<Trainee> foundTrainee = traineeRepository.findById(trainee.getId());

            assertTrue(foundTrainee.isPresent());
            assertEquals(trainee.getFirstName(), foundTrainee.get().getFirstName());
            assertEquals(trainee.getLastName(), foundTrainee.get().getLastName());
            assertEquals(trainee.getUserName(), foundTrainee.get().getUserName());
            assertEquals(trainee.getPasswordHash(), foundTrainee.get().getPasswordHash());
            assertEquals(trainee.isActive(), foundTrainee.get().isActive());
            assertEquals(trainee.getDateOfBirth(), foundTrainee.get().getDateOfBirth());
            assertEquals(trainee.getAddress(), foundTrainee.get().getAddress());
        } catch (Exception e) {
            fail("Exception executing test: ", e);
        } finally {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
        }
    }

    @Test
    void updateShouldThrowException_ifEntityWithSuchUserNameAlreadyExist() {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerStorage.getEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            deleteAll(entityManager);
            String userName = "John.Smith";
            Trainee trainee = Trainee.builder().firstName("John").lastName("Smith").userName(userName)
                    .passwordHash("john").isActive(true).dateOfBirth(LocalDate.of(1990, 1, 1)).address("USA").build();
            trainee = traineeRepository.create(trainee);

            Trainee newTrainee = traineeRepository.create(Trainee.builder().firstName("Jack").lastName("Robertson")
                    .userName("Jack.Robertson").passwordHash("jack").isActive(true)
                    .dateOfBirth(LocalDate.of(1995, 12, 1)).address("USA").build());

            newTrainee.setUserName(userName);
            Exception exc = assertThrows(ConstraintViolationException.class, () -> {
                traineeRepository.update(newTrainee);
                entityManager.flush();
            });
            assertTrue(exc.getMessage().contains("duplicate key value violates unique constraint"));
        } catch (Exception e) {
            fail("Exception executing test: ", e);
        } finally {
            if (transaction != null && transaction.isActive())
                transaction.setRollbackOnly();
        }
    }

    @Test
    void getTrainingListByUserNameDateRangeTrainerNameType_shouldReturnListOfTraining_ifSucceeds() {
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
                    .stream(traineeRepository.getTrainingListByUserNameDateRangeTrainerNameType(traineeName,
                            LocalDate.of(2023, 1, 1), LocalDate.of(2025, 1, 1), trainerName, trainingType))
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
