package com.tarosuke777.hms.mapper;

import com.tarosuke777.hms.entity.AuthorEntity;
import com.tarosuke777.hms.form.AuthorForm;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

  @IgnoreAuditFields
  AuthorEntity toEntity(AuthorForm form);

  AuthorForm toForm(AuthorEntity entity);

  AuthorEntity copy(AuthorEntity entity);

  @IgnoreAuditFields
  void updateEntityFromForm(AuthorForm form, @MappingTarget AuthorEntity entity);
}
