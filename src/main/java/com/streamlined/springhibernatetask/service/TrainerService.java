package com.streamlined.springhibernatetask.service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import dto.TrainerCreatedResponse;
import dto.TrainerDto;
import dto.TrainingDto;
import exception.EntityCreationException;
import exception.EntityDeletionException;
import exception.EntityQueryException;
import exception.EntityUpdateException;

public interface TrainerService {

    TrainerCreatedResponse create(TrainerDto dto) throws EntityCreationException;

    TrainerDto create(TrainerDto dto, char[] password) throws EntityCreationException;

    TrainerDto update(TrainerDto dto) throws EntityUpdateException;

    TrainerDto updatePassword(Long id, char[] password) throws EntityUpdateException;

    void deleteById(Long id) throws EntityDeletionException;

    Optional<TrainerDto> findById(Long id) throws EntityQueryException;

    Optional<TrainerDto> findByUserName(String userName) throws EntityQueryException;

    Stream<TrainerDto> findAll() throws EntityQueryException;

    boolean changeActiveStatus(Long id) throws EntityQueryException;

    Stream<TrainingDto> getTrainingListByUserNameDateRangeTraineeName(String trainerUserName, LocalDate fromDate,
            LocalDate toDate, String traineeName) throws EntityQueryException;

    Stream<TrainerDto> getNonAssignedTrainers(String traineeUserName);

}
