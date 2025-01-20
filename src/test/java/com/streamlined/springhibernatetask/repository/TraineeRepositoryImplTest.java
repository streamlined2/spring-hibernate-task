package com.streamlined.springhibernatetask.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
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

import com.streamlined.springhibernatetask.Utilities;
import com.streamlined.springhibernatetask.EntityManagerStorage;
import com.streamlined.springhibernatetask.SpringHibernateTaskApplication;
import com.streamlined.springhibernatetask.entity.Trainee;
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
            if (transaction != null)
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
            if (transaction != null)
                transaction.rollback();
        }
    }

    private void saveAll(Iterable<Trainee> trainees) {
        trainees.forEach(traineeRepository::create);
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
            if (transaction != null)
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
            if (transaction != null)
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
            if (transaction != null)
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
            if (transaction != null)
                transaction.rollback();
        }
    }

    @Test
    void testGetTrainingListByUserNameDateRangeTrainerNameType() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    void testDeleteByUserName() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    void testSave() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    void testFindById() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    void testDeleteById() {
        fail("Not yet implemented"); // TODO
    }

}
