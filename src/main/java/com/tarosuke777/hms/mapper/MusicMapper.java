package com.tarosuke777.hms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.tarosuke777.hms.entity.MusicEntity;
import com.tarosuke777.hms.form.MusicForm;

@Mapper(componentModel = "spring")
public interface MusicMapper {

    @IgnoreAuditFields
    @Mapping(target = "artist", ignore = true)
    MusicEntity toEntity(MusicForm form);

    @Mapping(target = "artistId", ignore = true)
    MusicForm toForm(MusicEntity entity);

    MusicEntity copy(MusicEntity entity);

    @IgnoreAuditFields
    @Mapping(target = "artist", ignore = true)
    void updateEntityFromForm(MusicForm form, @MappingTarget MusicEntity entity);
}
