package com.tarosuke777.hms.repository;

import com.tarosuke777.hms.entity.TrainingEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingRepository extends JpaRepository<TrainingEntity, Integer> {

  @EntityGraph(attributePaths = {"trainingMenu"})
  Optional<TrainingEntity> findByTrainingIdAndCreatedBy(Integer trainingId, Integer createdBy);

  @EntityGraph(attributePaths = {"trainingMenu"})
  List<TrainingEntity> findByCreatedBy(Integer createdBy, Sort sort);

  boolean existsByTrainingIdAndCreatedBy(Integer trainingId, Integer createdBy);
}
