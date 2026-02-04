package com.tarosuke777.hms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import com.tarosuke777.hms.entity.DiaryEntity;
import com.tarosuke777.hms.form.DiaryForm;

@Mapper(componentModel = "spring")
public interface DiaryMapper {

    @IgnoreAuditFields
    DiaryEntity toEntity(DiaryForm form);

    DiaryForm toForm(DiaryEntity entity);

    DiaryEntity copy(DiaryEntity entity);

    @IgnoreAuditFields
    void updateEntityFromForm(DiaryForm form, @MappingTarget DiaryEntity entity);
}
