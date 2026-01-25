package com.tarosuke777.hms.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tarosuke777.hms.entity.DiaryEntity;

@Repository
public interface DiaryRepository extends JpaRepository<DiaryEntity, Integer> {

    Optional<DiaryEntity> findByDiaryIdAndCreatedBy(Integer diaryId, String createdBy);

    List<DiaryEntity> findByCreatedBy(String createdBy, Sort sort);

    boolean existsByDiaryIdAndCreatedBy(Integer diaryId, String createdBy);
}
