package com.streamlined.springhibernatetask.repository;

import org.springframework.stereotype.Repository;

import com.streamlined.springhibernatetask.entity.TrainingType;

@Repository
public interface TrainingTypeRepository extends ReadOnlyRepository<TrainingType, Long> {
}
