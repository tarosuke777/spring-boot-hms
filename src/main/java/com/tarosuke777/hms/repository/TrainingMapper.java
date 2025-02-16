package com.tarosuke777.hms.repository;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tarosuke777.hms.entity.TrainingEntity;

@Mapper
public interface TrainingMapper {

	public List<TrainingEntity> findMany();

	public TrainingEntity findOne(Integer trainingId);

	public void insertOne(@Param("trainingDate") LocalDate trainingDate,
			@Param("trainingMenuId") Integer trainingMenuId, @Param("weight") Integer weight,
			@Param("reps") Integer reps, @Param("sets") Integer sets);

	public void updateOne(@Param("trainingId") Integer trainingId, @Param("trainingDate") LocalDate trainingDate,
			@Param("trainingMenuId") Integer trainingMenuId, @Param("weight") Integer weight,
			@Param("reps") Integer reps, @Param("sets") Integer sets);

	public int deleteOne(@Param("trainingId") Integer trainingId);
}
