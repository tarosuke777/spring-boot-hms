package com.tarosuke777.hms.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthorMapper {

  public void insertOne(@Param("authorName") String authorName);

}
