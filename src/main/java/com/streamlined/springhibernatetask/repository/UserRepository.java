package com.streamlined.springhibernatetask.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.streamlined.springhibernatetask.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query("""
            select max(u.userName)
            from User u
            where
                u.firstName=:firstName and
                u.lastName=:lastName
            """)
    Optional<String> getMaxUsername(@Param("firstName") String firstName, @Param("lastName") String lastName);

}
