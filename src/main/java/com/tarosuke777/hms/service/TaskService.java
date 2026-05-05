package com.tarosuke777.hms.service;

import java.util.List;
import java.util.Objects;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tarosuke777.hms.entity.TaskEntity;
import com.tarosuke777.hms.form.TaskForm;
import com.tarosuke777.hms.mapper.TaskMapper;
import com.tarosuke777.hms.repository.TaskRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public List<TaskForm> getTaskList(Integer currentUserId) {
        return taskRepository.findByCreatedBy(currentUserId).stream().map(taskMapper::toForm)
                .toList();
    }

    public TaskForm getTask(Integer id, Integer currentUserId) {
        TaskEntity entity = taskRepository.findByIdAndCreatedBy(id, currentUserId)
                .orElseThrow(() -> new RuntimeException("Task not found or unauthorized"));
        return taskMapper.toForm(entity);
    }

    @Transactional
    public void createTask(TaskForm form) {
        TaskEntity entity = Objects.requireNonNull(taskMapper.toEntity(form));
        taskRepository.save(entity);
    }

    @Transactional
    public void updateTask(TaskForm form, Integer currentUserId) {
        TaskEntity existEntity = taskRepository.findByIdAndCreatedBy(form.getId(), currentUserId)
                .orElseThrow(() -> new RuntimeException("Task not found or unauthorized"));
        TaskEntity entity = Objects.requireNonNull(taskMapper.copy(existEntity));
        taskMapper.updateEntityFromForm(form, entity);
        taskRepository.save(entity);
    }

    @Transactional
    public void deleteTask(@NonNull Integer id, Integer currentUserId) {

        if (!taskRepository.existsByIdAndCreatedBy(id, currentUserId)) {
            throw new RuntimeException("Task not found or unauthorized");
        }

        taskRepository.deleteById(id);
    }
}
