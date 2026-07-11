package com.tarosuke777.hms.repository;

import com.tarosuke777.hms.entity.MusicEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicRepository
    extends JpaRepository<MusicEntity, Integer>, JpaSpecificationExecutor<MusicEntity> {

  @EntityGraph(attributePaths = {"artist"})
  @Override
  @NonNull
  Page<MusicEntity> findAll(@Nullable Specification<MusicEntity> spec, @NonNull Pageable pageable);

  @EntityGraph(attributePaths = {"artist"})
  Optional<MusicEntity> findByIdAndCreatedBy(Integer id, Integer createdBy);

  @EntityGraph(attributePaths = {"artist"})
  List<MusicEntity> findByCreatedByOrderByIdAsc(Integer createdBy);

  boolean existsByIdAndCreatedBy(Integer id, Integer createdBy);
}
