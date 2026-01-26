package com.tarosuke777.hms.domain;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tarosuke777.hms.entity.TaskEntity;
import com.tarosuke777.hms.form.TaskForm;
import com.tarosuke777.hms.repository.TaskRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    public List<TaskForm> getTaskList(String currentUserId) {
        return taskRepository.findByCreatedBy(currentUserId).stream()
                .map(entity -> modelMapper.map(entity, TaskForm.class)).toList();
    }

    public TaskForm getTask(Integer id, String currentUserId) {
        TaskEntity entity = taskRepository.findByIdAndCreatedBy(id, currentUserId)
                .orElseThrow(() -> new RuntimeException("Task not found or unauthorized"));
        return modelMapper.map(entity, TaskForm.class);
    }

    @Transactional
    public void createTask(TaskForm form) {
        TaskEntity entity = modelMapper.map(form, TaskEntity.class);
        taskRepository.save(entity);
    }

    @Transactional
    public void updateTask(TaskForm form, String currentUserId) {
        TaskEntity existEntity = taskRepository.findByIdAndCreatedBy(form.getId(), currentUserId)
                .orElseThrow(() -> new RuntimeException("Task not found or unauthorized"));
        TaskEntity entity = new TaskEntity();
        modelMapper.map(existEntity, entity);
        modelMapper.map(form, entity);
        taskRepository.save(entity);
    }

    @Transactional
    public void deleteTask(Integer id, String currentUserId) {

        if (!taskRepository.existsByIdAndCreatedBy(id, currentUserId)) {
            throw new RuntimeException("Task not found or unauthorized");
        }

        taskRepository.deleteById(id);
    }
}
