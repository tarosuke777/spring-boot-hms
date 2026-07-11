package com.tarosuke777.hms.mapper;

import com.tarosuke777.hms.entity.TrainingEntity;
import com.tarosuke777.hms.form.TrainingForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TrainingMapper {

  @IgnoreAuditFields
  @Mapping(target = "trainingMenu", ignore = true)
  TrainingEntity toEntity(TrainingForm form);

  @Mapping(target = "trainingAreaId", ignore = true)
  @Mapping(target = "trainingMenuId", ignore = true)
  TrainingForm toForm(TrainingEntity entity);

  TrainingEntity copy(TrainingEntity entity);

  @IgnoreAuditFields
  @Mapping(target = "trainingMenu", ignore = true)
  void updateEntityFromForm(TrainingForm form, @MappingTarget TrainingEntity entity);
}
