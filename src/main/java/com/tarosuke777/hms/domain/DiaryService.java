package com.tarosuke777.hms.domain;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tarosuke777.hms.entity.DiaryEntity;
import com.tarosuke777.hms.form.DiaryForm;
import com.tarosuke777.hms.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final ModelMapper modelMapper;

    public List<DiaryForm> getDiaryList(String orderBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("desc") ? Sort.by(orderBy).descending()
                : Sort.by(orderBy).ascending();

        return diaryRepository.findAll(sort).stream()
                .map(entity -> modelMapper.map(entity, DiaryForm.class)).toList();
    }

    public DiaryForm getDiaryDetails(Integer diaryId) {
        DiaryEntity diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new RuntimeException("Diary not found"));
        return modelMapper.map(diary, DiaryForm.class);
    }

    @Transactional
    public void registerDiary(DiaryForm form) {
        DiaryEntity entity = modelMapper.map(form, DiaryEntity.class);
        diaryRepository.save(entity);
    }

    @Transactional
    public void updateDiary(DiaryForm form) {
        DiaryEntity existEntity = diaryRepository.findById(form.getDiaryId())
                .orElseThrow(() -> new RuntimeException("Diary not found"));
        DiaryEntity entity = new DiaryEntity();
        modelMapper.map(existEntity, entity);
        modelMapper.map(form, entity);
        diaryRepository.save(entity);
    }

    @Transactional
    public void deleteDiary(Integer diaryId) {
        diaryRepository.deleteById(diaryId);
    }
}
