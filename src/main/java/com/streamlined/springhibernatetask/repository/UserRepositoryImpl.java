package com.streamlined.springhibernatetask.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.streamlined.springhibernatetask.entity.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final EntityManagerFactory entityManagerFactory;

    @Override
    public Optional<String> getMaxUsername(String firstName, String lastName) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            TypedQuery<String> query = entityManager.createQuery("""
                    select max(u.userName)
                    from User u
                    where
                         u.firstName=:firstName and
                         u.lastName=:lastName
                    """, String.class);
            query.setParameter("firstName", firstName);
            query.setParameter("lastName", lastName);
            return Optional.ofNullable(query.getSingleResultOrNull());
        }
    }

    @Override
    public Iterable<User> findAll() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            TypedQuery<User> query = entityManager.createQuery("select t from User t", User.class);
            return query.getResultList();
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return Optional.ofNullable(entityManager.find(User.class, id));
        }
    }

    @Override
    public Iterable<User> findAllById(List<Long> ids) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            TypedQuery<User> query = entityManager.createQuery("select t from User t where t.id in :idList",
                    User.class);
            query.setParameter("idList", ids);
            return query.getResultList();
        }
    }

}
