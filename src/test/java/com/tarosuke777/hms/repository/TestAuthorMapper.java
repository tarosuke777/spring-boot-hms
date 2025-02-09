package com.tarosuke777.hms.repository;

import org.apache.ibatis.annotations.Mapper;

import com.tarosuke777.hms.entity.AuthorEntity;

@Mapper
public interface TestAuthorMapper {

  public AuthorEntity findFirstOne();

  public AuthorEntity findLastOne();
}
