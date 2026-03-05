package com.tarosuke777.hms.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tarosuke777.hms.entity.TrainingMenuEntity;

@Repository
public interface TrainingMenuRepository extends JpaRepository<TrainingMenuEntity, Integer> {
    Optional<TrainingMenuEntity> findByIdAndCreatedBy(Integer id, String createdBy);

    List<TrainingMenuEntity> findByCreatedBy(String createdBy);

    boolean existsByIdAndCreatedBy(Integer id, String createdBy);
}
