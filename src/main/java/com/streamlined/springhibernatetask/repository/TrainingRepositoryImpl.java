package com.streamlined.springhibernatetask.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.streamlined.springhibernatetask.EntityManagerStorage;
import com.streamlined.springhibernatetask.entity.Training;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TrainingRepositoryImpl implements TrainingRepository {

    private final EntityManagerStorage entityManagerStorage;

    @Override
    public Iterable<Training> findAll() {
        EntityManager entityManager = entityManagerStorage.getEntityManager();
        TypedQuery<Training> query = entityManager.createQuery("select t from Training t", Training.class);
        return query.getResultList();
    }

    @Override
    public Optional<Training> findById(Long id) {
        EntityManager entityManager = entityManagerStorage.getEntityManager();
        return Optional.ofNullable(entityManager.find(Training.class, id));
    }

    @Override
    public Training create(Training training) {
        EntityManager entityManager = entityManagerStorage.getEntityManager();
        entityManager.persist(training);
        return training;
    }

    @Override
    public Training update(Training training) {
        EntityManager entityManager = entityManagerStorage.getEntityManager();
        return entityManager.merge(training);
    }

}
