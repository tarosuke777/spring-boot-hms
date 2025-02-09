package com.tarosuke777.hms.repository;

import org.apache.ibatis.annotations.Mapper;

import com.tarosuke777.hms.entity.BookEntity;

@Mapper
public interface TestBookMapper {

  public BookEntity findFirstOne();

  public BookEntity findLastOne();
}
