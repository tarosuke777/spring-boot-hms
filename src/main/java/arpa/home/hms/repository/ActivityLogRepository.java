package arpa.home.hms.repository;

import arpa.home.hms.entity.ActivityLogEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLogEntity, Integer> {

  Page<ActivityLogEntity> findByCreatedByOrderByActivityDateDescIdDesc(Integer createdBy,
      Pageable pageable);

  Optional<ActivityLogEntity> findByIdAndCreatedBy(Integer id, Integer createdBy);

  boolean existsByIdAndCreatedBy(Integer id, Integer createdBy);

  void deleteByIdAndCreatedBy(Integer id, Integer createdBy);
}
