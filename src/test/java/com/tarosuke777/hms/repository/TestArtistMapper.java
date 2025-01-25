package com.tarosuke777.hms.repository;

import org.apache.ibatis.annotations.Mapper;

import com.tarosuke777.hms.entity.ArtistEntity;

@Mapper
public interface TestArtistMapper {

  public ArtistEntity findFirstOne();

  public ArtistEntity findLastOne();
}
