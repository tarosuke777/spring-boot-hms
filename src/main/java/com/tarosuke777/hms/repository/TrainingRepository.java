package com.tarosuke777.hms.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tarosuke777.hms.entity.TrainingEntity;

@Repository
public interface TrainingRepository extends JpaRepository<TrainingEntity, Integer> {

    @EntityGraph(attributePaths = {"trainingMenu"})
    Optional<TrainingEntity> findByTrainingIdAndCreatedBy(Integer trainingId, String createdBy);

    @EntityGraph(attributePaths = {"trainingMenu"})
    List<TrainingEntity> findByCreatedBy(String createdBy, Sort sort);

    boolean existsByTrainingIdAndCreatedBy(Integer trainingId, String createdBy);

}
