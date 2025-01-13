package com.streamlined.springhibernatetask.service;

import java.util.Optional;
import java.util.stream.Stream;

import dto.TraineeDto;
import exception.EntityCreationException;
import exception.EntityDeletionException;
import exception.EntityQueryException;
import exception.EntityUpdateException;

public interface TraineeService {

    void create(TraineeDto dto, char[] password) throws EntityCreationException;

    void update(TraineeDto dto) throws EntityUpdateException;

    void updatePassword(Long id, char[] password) throws EntityUpdateException;

    void deleteById(Long id) throws EntityDeletionException;

    Optional<TraineeDto> findById(Long id) throws EntityQueryException;

    Optional<TraineeDto> findByUserName(String userName) throws EntityQueryException;

    Stream<TraineeDto> findAll() throws EntityQueryException;

}
