package com.tarosuke777.hms.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tarosuke777.hms.entity.ArtistEntity;

@Mapper
public interface ArtistMapper {

  public List<ArtistEntity> findMany();

  public ArtistEntity findOne(Integer artistId);

  public void insertOne(@Param("artistName") String artistName);

  public void updateOne(@Param("artistId") Integer artistId,
      @Param("artistName") String artistName);

  public int deleteOne(@Param("artistId") Integer artistId);
}
