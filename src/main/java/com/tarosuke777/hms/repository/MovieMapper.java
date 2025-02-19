package com.tarosuke777.hms.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tarosuke777.hms.entity.MovieEntity;

@Mapper
public interface MovieMapper {

	public List<MovieEntity> findMany();

	public MovieEntity findOne(Integer movieId);

	public void insertOne(@Param("movieName") String movieName, @Param("note") String note);

	public void updateOne(@Param("movieId") Integer movieId, @Param("movieName") String movieName,
			@Param("note") String note);

	public int deleteOne(@Param("movieId") Integer movieId);
}
