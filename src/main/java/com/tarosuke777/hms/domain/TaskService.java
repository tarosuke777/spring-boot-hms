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

    public List<TaskForm> getTaskList() {
        return taskRepository.findAll().stream()
                .map(entity -> modelMapper.map(entity, TaskForm.class)).toList();
    }

    public TaskForm getTask(Integer id) {
        TaskEntity entity = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return modelMapper.map(entity, TaskForm.class);
    }

    @Transactional
    public void createTask(TaskForm form) {
        TaskEntity entity = modelMapper.map(form, TaskEntity.class);
        taskRepository.save(entity);
    }

    @Transactional
    public void updateTask(TaskForm form) {
        TaskEntity entity = modelMapper.map(form, TaskEntity.class);
        taskRepository.save(entity);
    }

    @Transactional
    public void deleteTask(Integer id) {
        taskRepository.deleteById(id);
    }
}
