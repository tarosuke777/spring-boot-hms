package arpa.home.hms.repository;

import arpa.home.hms.entity.ArtistEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<ArtistEntity, Integer> {

  Optional<ArtistEntity> findByIdAndCreatedBy(Integer id, Integer createdBy);

  List<ArtistEntity> findByCreatedBy(Integer createdBy);

  Page<ArtistEntity> findByCreatedBy(Integer createdBy, Pageable pageable);

  boolean existsByIdAndCreatedBy(Integer id, Integer createdBy);
}
