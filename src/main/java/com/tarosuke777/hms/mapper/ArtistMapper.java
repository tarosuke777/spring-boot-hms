package com.tarosuke777.hms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import com.tarosuke777.hms.entity.ArtistEntity;
import com.tarosuke777.hms.form.ArtistForm;

@Mapper(componentModel = "spring")
public interface ArtistMapper {

    @IgnoreAuditFields
    ArtistEntity toEntity(ArtistForm form);

    ArtistForm toForm(ArtistEntity entity);

    ArtistEntity copy(ArtistEntity entity);

    @IgnoreAuditFields
    void updateEntityFromForm(ArtistForm form, @MappingTarget ArtistEntity entity);
}
