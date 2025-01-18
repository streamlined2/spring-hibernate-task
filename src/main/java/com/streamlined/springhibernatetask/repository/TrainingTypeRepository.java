package com.streamlined.springhibernatetask.repository;

import java.util.Optional;

import com.streamlined.springhibernatetask.entity.TrainingType;

public interface TrainingTypeRepository {

    Optional<TrainingType> findById(Long id);

    Iterable<TrainingType> findAll();

    Iterable<TrainingType> findAllById(Iterable<Long> ids);

}
