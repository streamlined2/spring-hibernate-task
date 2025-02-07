package com.streamlined.springhibernatetask.repository;

import java.time.LocalDate;
import java.util.Optional;

import com.streamlined.springhibernatetask.entity.Trainee;
import com.streamlined.springhibernatetask.entity.Training;
import com.streamlined.springhibernatetask.entity.TrainingType;

public interface TraineeRepository {

    Iterable<Trainee> findAll();

    Optional<Trainee> findByUserName(String userName);

    Iterable<Training> getTrainingListByUserNameDateRangeTrainerNameType(String userName, LocalDate fromDate,
            LocalDate toDate, String trainerName, TrainingType trainingType);

    void deleteByUserName(String userName);

    Trainee create(Trainee trainee);

    Trainee update(Trainee trainee);

    Optional<Trainee> findById(Long id);

    void deleteById(Long id);

}
