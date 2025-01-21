package com.streamlined.springhibernatetask.service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import com.streamlined.springhibernatetask.dto.TraineeCreatedResponse;
import com.streamlined.springhibernatetask.dto.TraineeDto;
import com.streamlined.springhibernatetask.dto.TrainingDto;
import com.streamlined.springhibernatetask.entity.TrainingType;
import com.streamlined.springhibernatetask.exception.EntityCreationException;
import com.streamlined.springhibernatetask.exception.EntityDeletionException;
import com.streamlined.springhibernatetask.exception.EntityQueryException;
import com.streamlined.springhibernatetask.exception.EntityUpdateException;

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
