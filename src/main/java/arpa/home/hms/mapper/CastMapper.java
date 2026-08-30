package arpa.home.hms.mapper;

import arpa.home.hms.entity.CastEntity;
import arpa.home.hms.form.CastForm;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CastMapper {

  @IgnoreAuditFields
  CastEntity toEntity(CastForm form);

  CastForm toForm(CastEntity entity);

  CastEntity copy(CastEntity entity);

  @IgnoreAuditFields
  void updateEntityFromForm(CastForm form, @MappingTarget CastEntity entity);
}
