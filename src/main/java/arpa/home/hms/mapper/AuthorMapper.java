package arpa.home.hms.mapper;

import arpa.home.hms.entity.AuthorEntity;
import arpa.home.hms.form.AuthorForm;
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
