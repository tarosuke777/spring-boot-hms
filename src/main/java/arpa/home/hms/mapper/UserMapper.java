package arpa.home.hms.mapper;

import arpa.home.hms.entity.UserEntity;
import arpa.home.hms.form.UserForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @IgnoreAuditFields
  UserEntity toEntity(UserForm form);

  @Mapping(target = "password", ignore = true)
  UserForm toForm(UserEntity entity);

  UserEntity copy(UserEntity entity);

  @IgnoreAuditFields
  void updateEntityFromForm(UserForm form, @MappingTarget UserEntity entity);
}
