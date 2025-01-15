package repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import com.streamlined.springhibernatetask.entity.Trainee;
import com.streamlined.springhibernatetask.entity.Training;
import com.streamlined.springhibernatetask.entity.TrainingType;

@Repository
public interface TraineeRepository extends CrudRepository<Trainee, Long> {

    Streamable<Trainee> findAll();

    @Query("select t from Trainee t where t.userName=:userName")
    Optional<Trainee> findByUserName(@Param("userName") String userName);

    @Query("""
            select t
            from Training t join t.trainee e join t.trainer r
            where
                e.userName=:userName and
                t.date between :fromDate and :toDate and
                r.userName=:trainerName and
                t.type=:trainingType
            """)
    Streamable<Training> getTrainingListByUserNameDateRangeTrainerNameType(@Param("userName") String userName,
            @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate,
            @Param("traineeName") String trainerName, @Param("trainingType") TrainingType trainingType);

    @Query("""
            select max(t.getUsernameSerial())
            from Trainee t
            where
                t.firstName=:firstName and
                t.lastName=:lastName
            """)
    Optional<String> getMaxUsernameSerial(@Param("firstName") String firstName, @Param("lastName") String lastName);

    void deleteByUserName(String userName);

}
