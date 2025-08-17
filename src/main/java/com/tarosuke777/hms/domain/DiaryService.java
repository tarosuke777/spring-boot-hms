package com.tarosuke777.hms.domain;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tarosuke777.hms.entity.DiaryEntity;
import com.tarosuke777.hms.form.DiaryForm;
import com.tarosuke777.hms.repository.DiaryMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryMapper diaryMapper;

    public List<DiaryForm> getDiaryList(String orderBy, String sort) {
        return diaryMapper.findMany(orderBy, sort).stream()
                .map(entity -> {
                    DiaryForm form = new DiaryForm();
                    form.setDiaryId(entity.getDiaryId());
                    form.setDiaryDate(entity.getDiaryDate());
                    form.setTodoPlan(entity.getTodoPlan());
                    form.setTodoActual(entity.getTodoActual());
                    form.setFunPlan(entity.getFunPlan());
                    form.setFunActual(entity.getFunActual());
                    form.setCommentPlan(entity.getCommentPlan());
                    form.setCommentActual(entity.getCommentActual());
                    return form;
                })
                .collect(Collectors.toList());
    }

    public DiaryForm getDiaryDetails(Integer diaryId) {
        DiaryEntity diary = diaryMapper.findOne(diaryId);
        DiaryForm diaryForm = new DiaryForm();
        diaryForm.setDiaryId(diary.getDiaryId());
        diaryForm.setDiaryDate(diary.getDiaryDate());
        diaryForm.setTodoPlan(diary.getTodoPlan());
        diaryForm.setTodoActual(diary.getTodoActual());
        diaryForm.setFunPlan(diary.getFunPlan());
        diaryForm.setFunActual(diary.getFunActual());
        diaryForm.setCommentPlan(diary.getCommentPlan());
        diaryForm.setCommentActual(diary.getCommentActual());
        return diaryForm;
    }

    @Transactional
    public void registerDiary(DiaryForm form) {
        diaryMapper.insertOne(form.getDiaryDate(), form.getTodoPlan(), form.getTodoActual(), form.getFunPlan(),
                form.getFunActual(), form.getCommentPlan(), form.getCommentActual());
    }

    @Transactional
    public void updateDiary(DiaryForm form) {
        diaryMapper.updateOne(form.getDiaryId(), form.getDiaryDate(), form.getTodoPlan(), form.getTodoActual(),
                form.getFunPlan(), form.getFunActual(), form.getCommentPlan(), form.getCommentActual());
    }

    @Transactional
    public void deleteDiary(Integer diaryId) {
        diaryMapper.deleteOne(diaryId);
    }
}