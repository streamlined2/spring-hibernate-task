package repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streamlined.springhibernatetask.entity.Trainer;

@Repository
public interface TrainerRepository extends CrudRepository<Trainer, Long> {
}
