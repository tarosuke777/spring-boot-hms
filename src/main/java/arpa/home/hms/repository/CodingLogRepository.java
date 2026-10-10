package arpa.home.hms.repository;

import arpa.home.hms.entity.CodingLogEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodingLogRepository extends JpaRepository<CodingLogEntity, Integer> {

  Page<CodingLogEntity> findByCreatedByOrderBySolvedDateDescIdDesc(Integer createdBy,
      Pageable pageable);

  Optional<CodingLogEntity> findByIdAndCreatedBy(Integer id, Integer createdBy);

  boolean existsByIdAndCreatedBy(Integer id, Integer createdBy);

  void deleteByIdAndCreatedBy(Integer id, Integer createdBy);
}
