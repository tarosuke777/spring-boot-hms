package arpa.home.hms.mapper;

import arpa.home.hms.entity.CodingLogEntity;
import arpa.home.hms.form.CodingLogForm;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CodingLogMapper {

  @IgnoreAuditFields
  CodingLogEntity toEntity(CodingLogForm form);

  CodingLogForm toForm(CodingLogEntity entity);

  CodingLogEntity copy(CodingLogEntity entity);

  @IgnoreAuditFields
  void updateEntityFromForm(CodingLogForm form, @MappingTarget CodingLogEntity entity);
}
