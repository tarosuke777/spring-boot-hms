package arpa.home.hms.converter;

import arpa.home.hms.enums.TaskCategory;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TaskCategoryConverter implements AttributeConverter<TaskCategory, Integer> {
  @Override
  public Integer convertToDatabaseColumn(TaskCategory category) {
    return (category == null) ? null : category.getCode();
  }

  @Override
  public TaskCategory convertToEntityAttribute(Integer value) {
    return (value == null) ? null : TaskCategory.fromValue(value);
  }
}
