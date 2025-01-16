package com.streamlined.springhibernatetask.repository;

import java.util.Optional;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
public interface ReadOnlyRepository<T, K> extends Repository<T, K> {

    Optional<T> findById(K id);

    boolean existsById(K id);

    Iterable<T> findAll();

    Iterable<T> findAllById(Iterable<K> ids);

    long count();

}
