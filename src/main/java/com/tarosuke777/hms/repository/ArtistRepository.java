package com.tarosuke777.hms.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tarosuke777.hms.entity.ArtistEntity;

@Repository
public interface ArtistRepository extends JpaRepository<ArtistEntity, Integer> {

    Optional<ArtistEntity> findByIdAndCreatedBy(Integer id, Integer createdBy);

    List<ArtistEntity> findByCreatedBy(Integer createdBy);

    boolean existsByIdAndCreatedBy(Integer id, Integer createdBy);
}
