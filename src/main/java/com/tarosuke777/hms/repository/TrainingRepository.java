package com.tarosuke777.hms.repository;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import com.tarosuke777.hms.entity.TrainingEntity;

@Repository
public interface TrainingRepository extends JpaRepository<TrainingEntity, Integer> {

    /**
     * findMany に相当 EntityGraphでメニューも同時取得し、引数の Sort で並び替えます
     */
    @EntityGraph(attributePaths = {"trainingMenu"})
    @NonNull
    List<TrainingEntity> findAll(@NonNull Sort sort);
}
