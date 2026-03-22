package com.tarosuke777.hms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.tarosuke777.hms.entity.MovieEntity;
import com.tarosuke777.hms.form.MovieForm;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    @IgnoreAuditFields
    @Mapping(target = "cast", ignore = true)
    MovieEntity toEntity(MovieForm form);

    @Mapping(target = "castId", ignore = true)
    MovieForm toForm(MovieEntity entity);

    MovieEntity copy(MovieEntity entity);

    @IgnoreAuditFields
    @Mapping(target = "cast", ignore = true)
    void updateEntityFromForm(MovieForm form, @MappingTarget MovieEntity entity);
}
