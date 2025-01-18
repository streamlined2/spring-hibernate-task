package com.streamlined.springhibernatetask.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.streamlined.springhibernatetask.entity.Trainee;
import com.streamlined.springhibernatetask.entity.Training;
import com.streamlined.springhibernatetask.entity.TrainingType;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TraineeRepositoryImpl implements TraineeRepository {

    private final EntityManagerFactory entityManagerFactory;

    @Override
    public Iterable<Trainee> findAll() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            TypedQuery<Trainee> query = entityManager.createQuery("select t from Trainee t", Trainee.class);
            return query.getResultList();
        }
    }

    @Override
    public Optional<Trainee> findByUserName(String userName) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return findByUserName(entityManager, userName);
        }
    }

    private Optional<Trainee> findByUserName(EntityManager entityManager, String userName) {
        TypedQuery<Trainee> query = entityManager.createQuery("select t from Trainee t where t.userName=:userName",
                Trainee.class);
        query.setParameter("userName", userName);
        return Optional.ofNullable(query.getSingleResultOrNull());
    }

    @Override
    public Iterable<Training> getTrainingListByUserNameDateRangeTrainerNameType(String userName, LocalDate fromDate,
            LocalDate toDate, String trainerName, TrainingType trainingType) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            TypedQuery<Training> query = entityManager.createQuery("""
                    select t
                    from Training t join t.trainee e join t.trainer r
                    where
                        e.userName=:userName and
                        t.date between :fromDate and :toDate and
                        r.userName=:trainerName and
                        t.type=:trainingType
                    """, Training.class);
            query.setParameter("userName", userName);
            query.setParameter("fromDate", fromDate);
            query.setParameter("toDate", toDate);
            query.setParameter("trainerName", trainerName);
            query.setParameter("trainingType", trainingType);
            return query.getResultList();
        }
    }

    @Override
    public void deleteByUserName(String userName) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            findByUserName(entityManager, userName).ifPresent(entityManager::remove);
        }
    }

    @Override
    public Trainee save(Trainee trainee) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.merge(trainee);
        }
    }

    @Override
    public Optional<Trainee> findById(Long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return Optional.ofNullable(entityManager.find(Trainee.class, id));
        }
    }

    @Override
    public void deleteById(Long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.remove(Trainee.builder().id(id).build());
        }
    }

}
