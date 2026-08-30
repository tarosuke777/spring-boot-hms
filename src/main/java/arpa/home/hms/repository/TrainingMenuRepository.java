package arpa.home.hms.repository;

import arpa.home.hms.entity.TrainingMenuEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingMenuRepository extends JpaRepository<TrainingMenuEntity, Integer> {
  Optional<TrainingMenuEntity> findByIdAndCreatedBy(Integer id, Integer createdBy);

  List<TrainingMenuEntity> findByCreatedBy(Integer createdBy);

  boolean existsByIdAndCreatedBy(Integer id, Integer createdBy);
}
