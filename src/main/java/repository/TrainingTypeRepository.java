package repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streamlined.springhibernatetask.entity.TrainingType;

@Repository
public interface TrainingTypeRepository extends CrudRepository<TrainingType, Long> {
}
