package arpa.home.hms.repository;

import arpa.home.hms.entity.TaskEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {

  List<TaskEntity> findAll(@Nullable Specification<TaskEntity> spec);

  Optional<TaskEntity> findByIdAndCreatedBy(Integer id, Integer createdBy);

  List<TaskEntity> findByCreatedBy(Integer createdBy);

  boolean existsByIdAndCreatedBy(Integer id, Integer createdBy);
}
