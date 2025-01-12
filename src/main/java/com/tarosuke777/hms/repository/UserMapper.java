package com.tarosuke777.hms.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tarosuke777.hms.entity.UserEntity;

@Mapper
public interface UserMapper {

  public UserEntity findLoginUser(String email);

  public List<UserEntity> findMany();

  public UserEntity findOne(Integer userId);

  public void insertOne(UserEntity user);

  public void updateOne(UserEntity user);

  public void deleteOne(@Param("userId") Integer userId);
}
