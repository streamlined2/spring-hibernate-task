package com.streamlined.springhibernatetask.repository;

import java.util.Optional;
import org.springframework.stereotype.Repository;

import com.streamlined.springhibernatetask.entity.TrainingType;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TrainingTypeRepositoryImpl implements TrainingTypeRepository {

    private final EntityManagerFactory entityManagerFactory;

    @Override
    public Optional<TrainingType> findById(Long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return Optional.ofNullable(entityManager.find(TrainingType.class, id));
        }
    }

    @Override
    public Iterable<TrainingType> findAll() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            TypedQuery<TrainingType> query = entityManager.createQuery("select t from TrainingType t",
                    TrainingType.class);
            return query.getResultList();
        }
    }

    @Override
    public Iterable<TrainingType> findAllById(Iterable<Long> ids) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            TypedQuery<TrainingType> query = entityManager
                    .createQuery("select t from TrainingType t where t.id in :idList", TrainingType.class);
            query.setParameter("idList", ids);
            return query.getResultList();
        }
    }

}
