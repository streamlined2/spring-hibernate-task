package com.streamlined.springhibernatetask.repository;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import com.streamlined.springhibernatetask.entity.Trainer;
import com.streamlined.springhibernatetask.entity.Training;

@Repository
public interface TrainerRepository extends CrudRepository<Trainer, Long> {

    Streamable<Trainer> findAll();

    @Query("select t from Trainer t where t.userName=:userName")
    Optional<Trainer> findByUserName(@Param("userName") String userName);

    @Query("""
            select t
            from Training t join t.trainee e join t.trainer r
            where
                r.userName=:userName and
                t.date between :fromDate and :toDate and
                e.userName=:traineeName
            """)
    Streamable<Training> getTrainingListByUserNameDateRangeTraineeNameType(@Param("userName") String userName,
            @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate,
            @Param("traineeName") String traineeName);

    @Query("""
            select a
            from Trainer a
            where a.id not in (
                select distinct r.id
                from Training t join t.trainer r join t.trainee e
                where e.userName=:userName
            )
            """)
    Streamable<Trainer> getNonAssignedTrainers(@Param("userName") String traineeUserName);

}
