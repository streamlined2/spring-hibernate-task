package com.streamlined.springhibernatetask.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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

import com.streamlined.springhibernatetask.EntityManagerStorage;
import com.streamlined.springhibernatetask.SpringHibernateTaskApplication;
import com.streamlined.springhibernatetask.Utilities;
import com.streamlined.springhibernatetask.entity.Trainee;
import com.streamlined.springhibernatetask.entity.Trainer;
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
    private TrainerRepositoryImpl trainerRepository;

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

}
