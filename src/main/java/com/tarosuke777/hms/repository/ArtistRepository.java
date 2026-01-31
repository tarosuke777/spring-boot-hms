package com.tarosuke777.hms.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tarosuke777.hms.entity.ArtistEntity;

@Repository
public interface ArtistRepository extends JpaRepository<ArtistEntity, Integer> {

    Optional<ArtistEntity> findByIdAndCreatedBy(Integer id, String createdBy);

    List<ArtistEntity> findByCreatedBy(String createdBy);

    boolean existsByIdAndCreatedBy(Integer id, String createdBy);
}
