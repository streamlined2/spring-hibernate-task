package com.streamlined.springhibernatetask.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.streamlined.springhibernatetask.entity.Training;

@Repository
public class TrainingRepositoryImpl implements TrainingRepository {

    @Override
    public Iterable<Training> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Training save(Training training) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Training> findById(Long key) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

}
