package com.tarosuke777.hms.repository;

import org.apache.ibatis.annotations.Mapper;

import com.tarosuke777.hms.entity.MusicEntity;

@Mapper
public interface TestMusicMapper {

  public MusicEntity findFirstOne();

  public MusicEntity findLastOne();
}
