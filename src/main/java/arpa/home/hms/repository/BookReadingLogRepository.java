package arpa.home.hms.repository;

import arpa.home.hms.entity.BookReadingLogEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookReadingLogRepository extends JpaRepository<BookReadingLogEntity, Integer> {

  @EntityGraph(attributePaths = {"book"})
  Page<BookReadingLogEntity> findByCreatedByOrderByReadDateDesc(Integer createdBy,
      Pageable pageable);

  @EntityGraph(attributePaths = {"book"})
  List<BookReadingLogEntity> findByCreatedByOrderByReadDateDescIdDesc(Integer createdBy,
      Pageable pageable);

  @EntityGraph(attributePaths = {"book"})
  Optional<BookReadingLogEntity> findByIdAndCreatedBy(Integer id, Integer createdBy);

  boolean existsByIdAndCreatedBy(Integer id, Integer createdBy);
}
