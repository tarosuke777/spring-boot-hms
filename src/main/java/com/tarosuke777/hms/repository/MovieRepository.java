package com.tarosuke777.hms.repository;

import com.tarosuke777.hms.entity.MovieEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Integer> {

  Optional<MovieEntity> findByIdAndCreatedBy(Integer id, Integer createdBy);

  List<MovieEntity> findByCreatedBy(Integer createdBy);

  Page<MovieEntity> findByCreatedBy(Integer createdBy, Pageable pageable);

  boolean existsByIdAndCreatedBy(Integer id, Integer createdBy);
}
