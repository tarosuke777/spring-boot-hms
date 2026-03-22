package com.tarosuke777.hms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import com.tarosuke777.hms.entity.CastEntity;
import com.tarosuke777.hms.form.CastForm;

@Mapper(componentModel = "spring")
public interface CastMapper {

    @IgnoreAuditFields
    CastEntity toEntity(CastForm form);

    CastForm toForm(CastEntity entity);

    CastEntity copy(CastEntity entity);

    @IgnoreAuditFields
    void updateEntityFromForm(CastForm form, @MappingTarget CastEntity entity);
}
