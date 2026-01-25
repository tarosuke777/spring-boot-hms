package com.tarosuke777.hms.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tarosuke777.hms.entity.MusicEntity;

@Repository
public interface MusicRepository extends JpaRepository<MusicEntity, Integer> {

    @EntityGraph(attributePaths = {"artist"})
    Optional<MusicEntity> findByMusicIdAndCreatedBy(Integer musicId, String createdBy);

    @EntityGraph(attributePaths = {"artist"})
    List<MusicEntity> findByCreatedByOrderByMusicIdAsc(String createdBy);

    boolean existsByMusicIdAndCreatedBy(Integer musicId, String createdBy);

}
