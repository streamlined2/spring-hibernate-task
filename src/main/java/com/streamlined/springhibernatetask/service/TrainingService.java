package com.streamlined.springhibernatetask.service;

import java.util.Optional;
import java.util.stream.Stream;

import com.streamlined.springhibernatetask.dto.TrainingDto;
import com.streamlined.springhibernatetask.entity.TrainingKey;
import com.streamlined.springhibernatetask.exception.EntityCreationException;
import com.streamlined.springhibernatetask.exception.EntityQueryException;

public interface TrainingService {

    TrainingDto create(TrainingDto dto) throws EntityCreationException;

    Optional<TrainingDto> findById(TrainingKey key) throws EntityQueryException;

    Stream<TrainingDto> findAll() throws EntityQueryException;

}
