package com.tarosuke777.hms.domain;

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

    public List<DiaryForm> getDiaryList(String orderBy, String sortDirection, String currentUser) {
        Sort sort = sortDirection.equalsIgnoreCase("desc") ? Sort.by(orderBy).descending()
                : Sort.by(orderBy).ascending();

        return diaryRepository.findByCreatedBy(currentUser, sort).stream().map(diaryMapper::toForm)
                .toList();
    }

    public DiaryForm getDiaryDetails(Integer diaryId, String currentUser) {
        DiaryEntity diary = diaryRepository.findByDiaryIdAndCreatedBy(diaryId, currentUser)
                .orElseThrow(() -> new RuntimeException("Diary not found or access denied"));
        return diaryMapper.toForm(diary);
    }

    @Transactional
    public void registerDiary(DiaryForm form) {
        DiaryEntity entity = diaryMapper.toEntity(form);
        diaryRepository.save(entity);
    }

    @Transactional
    public void updateDiary(DiaryForm form, String currentUser) {
        DiaryEntity existEntity = diaryRepository
                .findByDiaryIdAndCreatedBy(form.getDiaryId(), currentUser)
                .orElseThrow(() -> new RuntimeException("Diary not found or access denied"));
        DiaryEntity entity = diaryMapper.copy(existEntity);
        diaryMapper.updateEntityFromForm(form, entity);
        diaryRepository.save(entity);
    }

    @Transactional
    public void deleteDiary(Integer diaryId, String currentUser) {
        if (!diaryRepository.existsByDiaryIdAndCreatedBy(diaryId, currentUser)) {
            throw new RuntimeException("Diary not found or access denied");
        }
        diaryRepository.deleteById(diaryId);
    }
}
