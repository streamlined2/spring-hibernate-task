package repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import com.streamlined.springhibernatetask.entity.Trainee;

@Repository
public interface TraineeRepository extends CrudRepository<Trainee, Long> {

    Streamable<Trainee> findAll();

    @Query("select t from Trainee t where t.userName=:userName")
    Optional<Trainee> findByUserName(@Param("userName") String userName);

    @Query("""
            select max(t.getUsernameSerial())
            from Trainee t
            where
                t.firstName=:firstName and
                t.lastName=:lastName
            """)
    Optional<String> getMaxUsernameSerial(@Param("firstName") String firstName, @Param("lastName") String lastName);

}
