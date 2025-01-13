package com.streamlined.springhibernatetask.service;

import java.util.Optional;
import java.util.stream.Stream;

import com.streamlined.springhibernatetask.entity.TrainingKey;

import dto.TrainingDto;
import exception.EntityCreationException;
import exception.EntityQueryException;

public interface TrainingService {

    void create(TrainingDto dto) throws EntityCreationException;

    Optional<TrainingDto> findById(TrainingKey key) throws EntityQueryException;

    Stream<TrainingDto> findAll() throws EntityQueryException;

}
