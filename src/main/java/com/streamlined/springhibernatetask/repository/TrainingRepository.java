package com.streamlined.springhibernatetask.repository;

import java.util.Optional;

import com.streamlined.springhibernatetask.entity.Training;

public interface TrainingRepository {

    Iterable<Training> findAll();

    Training create(Training training);

    Training update(Training training);

    Optional<Training> findById(Long id);

}
