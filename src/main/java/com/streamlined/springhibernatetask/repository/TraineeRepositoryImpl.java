package com.streamlined.springhibernatetask.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.streamlined.springhibernatetask.EntityManagerStorage;
import com.streamlined.springhibernatetask.entity.Trainee;
import com.streamlined.springhibernatetask.entity.Training;
import com.streamlined.springhibernatetask.entity.TrainingType;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TraineeRepositoryImpl implements TraineeRepository {

    private final EntityManagerStorage entityManagerStorage;

    @Override
    public Iterable<Trainee> findAll() {
        EntityManager entityManager = entityManagerStorage.getEntityManager();
        TypedQuery<Trainee> query = entityManager.createQuery("select t from Trainee t", Trainee.class);
        return query.getResultList();
    }

    @Override
    public Optional<Trainee> findByUserName(String userName) {
        EntityManager entityManager = entityManagerStorage.getEntityManager();
        return findByUserName(entityManager, userName);
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
        EntityManager entityManager = entityManagerStorage.getEntityManager();
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

    @Override
    public void deleteByUserName(String userName) {
        EntityManager entityManager = entityManagerStorage.getEntityManager();
        findByUserName(entityManager, userName).ifPresent(entityManager::remove);
    }

    @Override
    public Optional<Trainee> findById(Long id) {
        EntityManager entityManager = entityManagerStorage.getEntityManager();
        return Optional.ofNullable(entityManager.find(Trainee.class, id));
    }

    @Override
    public void deleteById(Long id) {
        EntityManager entityManager = entityManagerStorage.getEntityManager();
        Trainee trainee = entityManager.getReference(Trainee.class, id);
        entityManager.remove(trainee);
    }

    @Override
    public Trainee create(Trainee trainee) {
        EntityManager entityManager = entityManagerStorage.getEntityManager();
        entityManager.persist(trainee);
        return trainee;
    }

    @Override
    public Trainee update(Trainee trainee) {
        EntityManager entityManager = entityManagerStorage.getEntityManager();
        return entityManager.merge(trainee);
    }

}
