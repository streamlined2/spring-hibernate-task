package repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streamlined.springhibernatetask.entity.Trainee;

@Repository
public interface TraineeRepository extends CrudRepository<Trainee, Long> {
}
