package com.streamlined.springhibernatetask.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.streamlined.springhibernatetask.entity.Training;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TrainingRepositoryImpl implements TrainingRepository {

    private final EntityManagerFactory entityManagerFactory;

    @Override
    public Iterable<Training> findAll() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            TypedQuery<Training> query = entityManager.createQuery("select t from Training t", Training.class);
            return query.getResultList();
        }
    }

    @Override
    public Training save(Training training) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.merge(training);
        }
    }

    @Override
    public Optional<Training> findById(Long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return Optional.ofNullable(entityManager.find(Training.class, id));
        }
    }

}
