package com.tarosuke777.hms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import com.tarosuke777.hms.entity.TrainingMenuEntity;
import com.tarosuke777.hms.form.TrainingMenuForm;

@Mapper(componentModel = "spring")
public interface TrainingMenuMapper {

    @IgnoreAuditFields
    TrainingMenuEntity toEntity(TrainingMenuForm form);

    TrainingMenuForm toForm(TrainingMenuEntity entity);

    TrainingMenuEntity copy(TrainingMenuEntity entity);

    @IgnoreAuditFields
    void updateEntityFromForm(TrainingMenuForm form, @MappingTarget TrainingMenuEntity entity);

}
