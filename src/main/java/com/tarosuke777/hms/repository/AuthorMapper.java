package com.tarosuke777.hms.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tarosuke777.hms.entity.AuthorEntity;

@Mapper
public interface AuthorMapper {

  public List<AuthorEntity> findMany();

  public AuthorEntity findOne(Integer authorId);

  public void insertOne(@Param("authorName") String authorName);

  public void updateOne(@Param("authorId") Integer authorId,
      @Param("authorName") String authorName);

  public int deleteOne(@Param("authorId") Integer authorId);

}
