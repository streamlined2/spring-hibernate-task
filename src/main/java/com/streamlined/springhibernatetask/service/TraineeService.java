package com.streamlined.springhibernatetask.service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import com.streamlined.springhibernatetask.entity.TrainingType;

import dto.TraineeCreatedResponse;
import dto.TraineeDto;
import dto.TrainingDto;
import exception.EntityCreationException;
import exception.EntityDeletionException;
import exception.EntityQueryException;
import exception.EntityUpdateException;

public interface TraineeService {

    TraineeCreatedResponse create(TraineeDto dto) throws EntityCreationException;

    TraineeDto create(TraineeDto dto, char[] password) throws EntityCreationException;

    TraineeDto update(TraineeDto dto) throws EntityUpdateException;

    TraineeDto updatePassword(Long id, char[] password) throws EntityUpdateException;

    void deleteById(Long id) throws EntityDeletionException;

    void deleteByUserName(String userName) throws EntityDeletionException;

    Optional<TraineeDto> findById(Long id) throws EntityQueryException;

    Optional<TraineeDto> findByUserName(String userName) throws EntityQueryException;

    Stream<TraineeDto> findAll() throws EntityQueryException;

    boolean changeActiveStatus(Long id) throws EntityQueryException;

    Stream<TrainingDto> getTrainingListByUserNameDateRangeTrainerNameType(String traineeUserName, LocalDate fromDate,
            LocalDate toDate, String trainerName, TrainingType trainingType) throws EntityQueryException;

}
