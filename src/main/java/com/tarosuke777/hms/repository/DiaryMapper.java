package com.tarosuke777.hms.repository;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tarosuke777.hms.entity.DiaryEntity;

@Mapper
public interface DiaryMapper {

    public List<DiaryEntity> findMany();

    public DiaryEntity findOne(Integer diaryId);

    public void insertOne(@Param("diaryDate") LocalDate diaryDate,
                          @Param("todoPlan") String todoPlan,
                          @Param("todoActual") String todoActual,
                          @Param("funPlan") Integer funPlan,
                          @Param("funActual") Integer funActual,
                          @Param("commentPlan") String commentPlan,
                          @Param("commentActual") String commentActual);

    public void updateOne(@Param("diaryId") Integer diaryId,
                          @Param("diaryDate") LocalDate diaryDate,
                          @Param("todoPlan") String todoPlan,
                          @Param("todoActual") String todoActual,
                          @Param("funPlan") Integer funPlan,
                          @Param("funActual") Integer funActual,
                          @Param("commentPlan") String commentPlan,
                          @Param("commentActual") String commentActual);

    public int deleteOne(@Param("diaryId") Integer diaryId);
}