package repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import com.streamlined.springhibernatetask.entity.Trainer;

@Repository
public interface TrainerRepository extends CrudRepository<Trainer, Long> {

    Streamable<Trainer> findAll();

    @Query("select t from Trainer t where t.userName=:userName")
    Optional<Trainer> findByUserName(@Param("userName") String userName);

    @Query("""
            select max(t.getUsernameSerial())
            from Trainer t
            where
                t.firstName=:firstName and
                t.lastName=:lastName
            """)
    Optional<String> getMaxUsernameSerial(@Param("firstName") String firstName, @Param("lastName") String lastName);

}
