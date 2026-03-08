package com.tarosuke777.hms.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tarosuke777.hms.entity.DiaryEntity;
import com.tarosuke777.hms.form.DiaryForm;
import com.tarosuke777.hms.mapper.DiaryMapper;
import com.tarosuke777.hms.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final DiaryMapper diaryMapper;

    public List<DiaryForm> getDiaryList(String orderBy, String sortDirection,
            Integer currentUserId) {
        Sort sort = sortDirection.equalsIgnoreCase("desc") ? Sort.by(orderBy).descending()
                : Sort.by(orderBy).ascending();

        return diaryRepository.findByCreatedBy(currentUserId, sort).stream()
                .map(diaryMapper::toForm).toList();
    }

    public DiaryForm getDiaryDetails(Integer diaryId, Integer currentUserId) {
        DiaryEntity diary = diaryRepository.findByDiaryIdAndCreatedBy(diaryId, currentUserId)
                .orElseThrow(() -> new RuntimeException("Diary not found or access denied"));
        return diaryMapper.toForm(diary);
    }

    @Transactional
    public void registerDiary(DiaryForm form) {
        DiaryEntity entity = diaryMapper.toEntity(form);
        diaryRepository.save(entity);
    }

    @Transactional
    public void updateDiary(DiaryForm form, Integer currentUserId) {
        DiaryEntity existEntity = diaryRepository
                .findByDiaryIdAndCreatedBy(form.getDiaryId(), currentUserId)
                .orElseThrow(() -> new RuntimeException("Diary not found or access denied"));
        DiaryEntity entity = diaryMapper.copy(existEntity);
        diaryMapper.updateEntityFromForm(form, entity);
        diaryRepository.save(entity);
    }

    @Transactional
    public void deleteDiary(Integer diaryId, Integer currentUserId) {
        if (!diaryRepository.existsByDiaryIdAndCreatedBy(diaryId, currentUserId)) {
            throw new RuntimeException("Diary not found or access denied");
        }
        diaryRepository.deleteById(diaryId);
    }
}
