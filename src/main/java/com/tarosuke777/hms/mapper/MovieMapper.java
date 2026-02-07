package com.tarosuke777.hms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import com.tarosuke777.hms.entity.MovieEntity;
import com.tarosuke777.hms.form.MovieForm;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    @IgnoreAuditFields
    MovieEntity toEntity(MovieForm form);

    MovieForm toForm(MovieEntity entity);

    MovieEntity copy(MovieEntity entity);

    @IgnoreAuditFields
    void updateEntityFromForm(MovieForm form, @MappingTarget MovieEntity entity);
}
