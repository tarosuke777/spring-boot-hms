package com.tarosuke777.hms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import com.tarosuke777.hms.entity.TaskEntity;
import com.tarosuke777.hms.form.TaskForm;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @IgnoreAuditFields
    TaskEntity toEntity(TaskForm form);

    TaskForm toForm(TaskEntity entity);

    TaskEntity copy(TaskEntity entity);

    @IgnoreAuditFields
    void updateEntityFromForm(TaskForm form, @MappingTarget TaskEntity entity);

}
