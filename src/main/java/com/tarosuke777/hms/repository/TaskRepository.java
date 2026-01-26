package com.tarosuke777.hms.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tarosuke777.hms.entity.TaskEntity;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {

    Optional<TaskEntity> findByIdAndCreatedBy(Integer id, String createdBy);

    List<TaskEntity> findByCreatedBy(String createdBy);

    boolean existsByIdAndCreatedBy(Integer id, String createdBy);
}
