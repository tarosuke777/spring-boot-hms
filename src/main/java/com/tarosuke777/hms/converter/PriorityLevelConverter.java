package com.tarosuke777.hms.converter;

import com.tarosuke777.hms.enums.PriorityLevel;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PriorityLevelConverter implements AttributeConverter<PriorityLevel, Integer> {
  @Override
  public Integer convertToDatabaseColumn(PriorityLevel priority) {
    return (priority == null) ? null : priority.getCode();
  }

  @Override
  public PriorityLevel convertToEntityAttribute(Integer value) {
    return (value == null) ? null : PriorityLevel.fromValue(value);
  }
}
