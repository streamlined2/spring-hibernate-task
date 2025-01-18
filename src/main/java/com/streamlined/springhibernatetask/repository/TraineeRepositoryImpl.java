package com.streamlined.springhibernatetask.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.streamlined.springhibernatetask.entity.Trainee;
import com.streamlined.springhibernatetask.entity.Training;
import com.streamlined.springhibernatetask.entity.TrainingType;

@Repository
public class TraineeRepositoryImpl implements TraineeRepository {

    @Override
    public Iterable<Trainee> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    // @Query("select t from Trainee t where t.userName=:userName")
    public Optional<Trainee> findByUserName(String userName) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    /*
     * @Query(""" select t from Training t join t.trainee e join t.trainer r where
     * e.userName=:userName and t.date between :fromDate and :toDate and
     * r.userName=:trainerName and t.type=:trainingType """)
     */
    public Iterable<Training> getTrainingListByUserNameDateRangeTrainerNameType(String userName, LocalDate fromDate,
            LocalDate toDate, String trainerName, TrainingType trainingType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteByUserName(String userName) {
        // TODO Auto-generated method stub

    }

    @Override
    public Trainee save(Trainee trainee) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Trainee> findById(Long userId) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {
        // TODO Auto-generated method stub
        
    }

}
