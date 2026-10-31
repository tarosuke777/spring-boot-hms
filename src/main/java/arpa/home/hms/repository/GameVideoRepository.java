package arpa.home.hms.repository;

import arpa.home.hms.entity.GameVideoEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface GameVideoRepository extends Repository<GameVideoEntity, Long> {

  @Query("SELECT video FROM GameVideoEntity video "
      + "WHERE (:title IS NULL OR video.title = :title) "
      + "AND (:name IS NULL OR video.name = :name) "
      + "ORDER BY video.createdAt DESC, video.id DESC")
  Page<GameVideoEntity> findAllByTitleAndName(@Param("title") String title,
      @Param("name") String name, Pageable pageable);

  @Query("SELECT DISTINCT video.title FROM GameVideoEntity video ORDER BY video.title")
  List<String> findDistinctTitles();

  @Query("SELECT DISTINCT video.name FROM GameVideoEntity video "
      + "WHERE video.name IS NOT NULL AND video.name <> '' "
      + "AND (:title IS NULL OR video.title = :title) ORDER BY video.name")
  List<String> findDistinctNamesByTitle(@Param("title") String title);

  List<GameVideoEntity> findAllByOrderByTitleAscIdAsc();

  Optional<GameVideoEntity> findById(Long id);
}
