package com.streamlined.springhibernatetask.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.streamlined.springhibernatetask.entity.Trainer;
import com.streamlined.springhibernatetask.entity.Training;

@Repository
public class TrainerRepositoryImpl implements TrainerRepository {

    @Override
    public Iterable<Trainer> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    // @Query("select t from Trainer t where t.userName=:userName")
    public Optional<Trainer> findByUserName(String userName) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    /*
     * @Query(""" select t from Training t join t.trainee e join t.trainer r where
     * r.userName=:userName and t.date between :fromDate and :toDate and
     * e.userName=:traineeName """)
     */
    public Iterable<Training> getTrainingListByUserNameDateRangeTraineeNameType(String userName, LocalDate fromDate,
            LocalDate toDate, String traineeName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    /*
     * @Query(""" select a from Trainer a where a.id not in ( select distinct r.id
     * from Training t join t.trainer r join t.trainee e where e.userName=:userName
     * ) """)
     */
    public Iterable<Trainer> getNonAssignedTrainers(String traineeUserName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Trainer save(Trainer trainer) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Trainer> findById(Long userId) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {
        // TODO Auto-generated method stub
        
    }

}
