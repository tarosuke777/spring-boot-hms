package com.tarosuke777.hms.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tarosuke777.hms.entity.TrainingEntity;

@Mapper
public interface TrainingMapper {

	public List<TrainingEntity> findMany();

	public TrainingEntity findOne(Integer trainingId);

	public void insertOne(@Param("trainingDate") LocalDate trainingDate, @Param("trainingMenuId") Integer trainingMenuId,
			@Param("weight") Integer weight, @Param("reps") Integer reps);

	public void updateOne(@Param("trainingId") Integer trainingId, @Param("trainingDate") LocalDate trainingDate,
			@Param("trainingMenuId") Integer trainingMenuId, @Param("weight") Integer weight, @Param("reps") Integer reps);

	public int deleteOne(@Param("trainingId") Integer trainingId);
}
