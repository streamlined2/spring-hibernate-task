package com.streamlined.springhibernatetask.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.streamlined.springhibernatetask.entity.Trainer;
import com.streamlined.springhibernatetask.entity.Training;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TrainerRepositoryImpl implements TrainerRepository {

    private final EntityManagerFactory entityManagerFactory;

    @Override
    public Iterable<Trainer> findAll() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            TypedQuery<Trainer> query = entityManager.createQuery("select t from Trainer t", Trainer.class);
            return query.getResultList();
        }
    }

    @Override
    public Optional<Trainer> findByUserName(String userName) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            TypedQuery<Trainer> query = entityManager.createQuery("select t from Trainer t where t.userName=:userName",
                    Trainer.class);
            query.setParameter("userName", userName);
            return Optional.ofNullable(query.getSingleResultOrNull());
        }
    }

    @Override
    public Iterable<Training> getTrainingListByUserNameDateRangeTraineeNameType(String userName, LocalDate fromDate,
            LocalDate toDate, String traineeName) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            TypedQuery<Training> query = entityManager.createQuery("""
                    select t
                    from Training t join t.trainee e join t.trainer r
                    where
                        r.userName=:userName and
                        t.date between :fromDate and :toDate and
                        e.userName=:traineeName
                    """, Training.class);
            query.setParameter("userName", userName);
            query.setParameter("fromDate", fromDate);
            query.setParameter("toDate", toDate);
            query.setParameter("traineeName", traineeName);
            return query.getResultList();
        }
    }

    @Override
    public Iterable<Trainer> getNonAssignedTrainers(String traineeUserName) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            TypedQuery<Trainer> query = entityManager.createQuery("""
                    select a
                    from Trainer a
                    where a.id not in (
                        select distinct r.id
                        from Training t join t.trainer r join t.trainee e
                        where e.userName=:traineeUserName
                    )
                    """, Trainer.class);
            query.setParameter("traineeUserName", traineeUserName);
            return query.getResultList();
        }
    }

    @Override
    public Trainer save(Trainer trainer) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.merge(trainer);
        }
    }

    @Override
    public Optional<Trainer> findById(Long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return Optional.ofNullable(entityManager.find(Trainer.class, id));
        }
    }

    @Override
    public void deleteById(Long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.remove(Trainer.builder().id(id).build());
        }
    }

}
