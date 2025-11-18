package com.tarosuke777.hms.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.tarosuke777.hms.entity.TaskEntity;

@Mapper
public interface TaskRepository {
    List<TaskEntity> findAll();

    TaskEntity findById(Integer id);

    void create(TaskEntity taskEntity);

    void update(TaskEntity taskEntity);

    void delete(Integer id);
}
