package com.streamlined.springhibernatetask.service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import com.streamlined.springhibernatetask.dto.TrainerCreatedResponse;
import com.streamlined.springhibernatetask.dto.TrainerDto;
import com.streamlined.springhibernatetask.dto.TrainingDto;
import com.streamlined.springhibernatetask.exception.EntityCreationException;
import com.streamlined.springhibernatetask.exception.EntityDeletionException;
import com.streamlined.springhibernatetask.exception.EntityQueryException;
import com.streamlined.springhibernatetask.exception.EntityUpdateException;

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
