package com.tarosuke777.hms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.tarosuke777.hms.entity.UserEntity;
import com.tarosuke777.hms.form.UserForm;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @IgnoreAuditFields
    @Mapping(target = "role", ignore = true)
    UserEntity toEntity(UserForm form);

    UserForm toForm(UserEntity entity);

    UserEntity copy(UserEntity entity);

    @IgnoreAuditFields
    @Mapping(target = "role", ignore = true)
    void updateEntityFromForm(UserForm form, @MappingTarget UserEntity entity);

}
