package com.tarosuke777.hms.converter;

import com.tarosuke777.hms.enums.TaskStatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TaskStatusConverter implements AttributeConverter<TaskStatus, Integer> {
  @Override
  public Integer convertToDatabaseColumn(TaskStatus status) {
    return (status == null) ? null : status.getCode();
  }

  @Override
  public TaskStatus convertToEntityAttribute(Integer value) {
    return (value == null) ? null : TaskStatus.fromValue(value);
  }
}
