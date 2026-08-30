package arpa.home.hms.mapper;

import arpa.home.hms.entity.TaskEntity;
import arpa.home.hms.form.TaskForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskMapper {

  @IgnoreAuditFields
  TaskEntity toEntity(TaskForm form);

  @Mapping(target = "searchStatus", ignore = true)
  @Mapping(target = "searchCategory", ignore = true)
  TaskForm toForm(TaskEntity entity);

  TaskEntity copy(TaskEntity entity);

  @IgnoreAuditFields
  void updateEntityFromForm(TaskForm form, @MappingTarget TaskEntity entity);
}
