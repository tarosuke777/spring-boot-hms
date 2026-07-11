package com.tarosuke777.hms.repository;

import com.tarosuke777.hms.entity.CastEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CastRepository extends JpaRepository<CastEntity, Integer> {

  Optional<CastEntity> findByIdAndCreatedBy(Integer id, Integer createdBy);

  List<CastEntity> findByCreatedBy(Integer createdBy);

  boolean existsByIdAndCreatedBy(Integer id, Integer createdBy);
}
