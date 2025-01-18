package com.streamlined.springhibernatetask.repository;

import java.util.List;
import java.util.Optional;

import com.streamlined.springhibernatetask.entity.User;

public interface UserRepository {

    Optional<String> getMaxUsername(String firstName, String lastName);

    Iterable<User> findAll();

    Optional<User> findById(Long id);

    Iterable<User> findAllById(List<Long> ids);

}
