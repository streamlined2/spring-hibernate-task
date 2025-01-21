package com.streamlined.springhibernatetask;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EntityManagerStorage {

    private final EntityManagerFactory entityManagerFactory;

    private static final ThreadLocal<EntityManager> ENTITY_MANAGER_LIST = new ThreadLocal<>();

    public EntityManager getEntityManager() {
        EntityManager entityManager = ENTITY_MANAGER_LIST.get();
        if (entityManager == null || !entityManager.isOpen()) {
            entityManager = entityManagerFactory.createEntityManager();
            ENTITY_MANAGER_LIST.set(entityManager);
        }
        return entityManager;
    }

    public void closeEntityManager() {
        EntityManager entityManager = ENTITY_MANAGER_LIST.get();
        if (entityManager != null) {
            entityManager.close();
            ENTITY_MANAGER_LIST.remove();
        }
    }

}
