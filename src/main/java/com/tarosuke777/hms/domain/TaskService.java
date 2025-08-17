package com.tarosuke777.hms.domain;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tarosuke777.hms.entity.TaskEntity;
import com.tarosuke777.hms.repository.TaskRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public List<TaskEntity> findAll() {
        return taskRepository.findAll();
    }

    public TaskEntity findById(Integer id) {
        return taskRepository.findById(id);
    }

    @Transactional
    public void create(TaskEntity taskEntity) {
        taskRepository.create(taskEntity);
    }

    @Transactional
    public void update(TaskEntity taskEntity) {
        taskRepository.update(taskEntity);
    }

    @Transactional
    public void delete(Integer id) {
        taskRepository.delete(id);
    }
}
