package com.tarosuke777.hms.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tarosuke777.hms.entity.TrainingMenuEntity;

@Mapper
public interface TrainingMenuMapper {

	public List<TrainingMenuEntity> findMany();

	public TrainingMenuEntity findOne(Integer trainingMenuId);

	public void insertOne(@Param("trainingMenuName") String trainingMenuName,
			@Param("targetAreaId") Integer targetAreaId, @Param("link") String link);

	public void updateOne(@Param("trainingMenuId") Integer trainingMenuId,
			@Param("trainingMenuName") String trainingMenuName, @Param("targetAreaId") Integer targetAreaId,
			@Param("link") String link);

	public int deleteOne(@Param("trainingMenuId") Integer trainingMenuId);
}
