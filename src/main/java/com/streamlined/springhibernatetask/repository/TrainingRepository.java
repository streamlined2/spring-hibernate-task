package com.streamlined.springhibernatetask.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import com.streamlined.springhibernatetask.entity.Training;

@Repository
public interface TrainingRepository extends CrudRepository<Training, Long> {
    Streamable<Training> findAll();
}
