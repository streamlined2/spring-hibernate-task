package com.streamlined.springhibernatetask.repository;

import java.time.LocalDate;
import java.util.Optional;

import com.streamlined.springhibernatetask.entity.Trainer;
import com.streamlined.springhibernatetask.entity.Training;

public interface TrainerRepository {

    Iterable<Trainer> findAll();

    Optional<Trainer> findByUserName(String userName);

    Iterable<Training> getTrainingListByUserNameDateRangeTraineeNameType(String userName, LocalDate fromDate,
            LocalDate toDate, String traineeName);

    Iterable<Trainer> getNonAssignedTrainers(String traineeUserName);

    Trainer create(Trainer trainer);

    Trainer update(Trainer trainer);

    Optional<Trainer> findById(Long userId);

    void deleteById(Long id);

}
