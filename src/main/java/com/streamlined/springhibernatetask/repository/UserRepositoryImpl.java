package com.streamlined.springhibernatetask.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.streamlined.springhibernatetask.entity.User;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Override
    /*
     * @Query(""" select max(u.userName) from User u where u.firstName=:firstName
     * and u.lastName=:lastName """)
     */
    public Optional<String> getMaxUsername(String firstName, String lastName) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public Iterable<User> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<User> findById(long l) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public Iterable<User> findAllById(List<Long> ids) {
        // TODO Auto-generated method stub
        return null;
    }

}
