package arpa.home.hms.repository;

import arpa.home.hms.entity.GameVideoEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

public interface GameVideoRepository extends Repository<GameVideoEntity, Long> {

  Page<GameVideoEntity> findAllByOrderByCreatedAtDescIdDesc(Pageable pageable);

  Optional<GameVideoEntity> findById(Long id);
}
