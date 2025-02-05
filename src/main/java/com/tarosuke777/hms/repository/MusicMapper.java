package com.tarosuke777.hms.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tarosuke777.hms.entity.MusicEntity;

@Mapper
public interface MusicMapper {

  public List<MusicEntity> findMany();

  public MusicEntity findOne(Integer musicId);

  public void insertOne(@Param("musicName") String musicName, @Param("artistId") Integer artistId, @Param("link") String link);

  public void updateOne(
      @Param("musicId") Integer musicId,
      @Param("musicName") String musicName,
      @Param("artistId") Integer artistId,
      @Param("link") String link);

  public int deleteOne(@Param("musicId") Integer musicId);
}
