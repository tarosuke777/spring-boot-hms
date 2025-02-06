package com.tarosuke777.hms.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tarosuke777.hms.entity.AuthorEntity;

@Mapper
public interface AuthorMapper {

  public List<AuthorEntity> findMany();

  public void insertOne(@Param("authorName") String authorName);

}
