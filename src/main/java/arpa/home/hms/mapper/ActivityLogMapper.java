package arpa.home.hms.mapper;

import arpa.home.hms.entity.ActivityLogEntity;
import arpa.home.hms.form.ActivityLogForm;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ActivityLogMapper {

  @IgnoreAuditFields
  ActivityLogEntity toEntity(ActivityLogForm form);

  ActivityLogForm toForm(ActivityLogEntity entity);

  ActivityLogEntity copy(ActivityLogEntity entity);

  @IgnoreAuditFields
  void updateEntityFromForm(ActivityLogForm form, @MappingTarget ActivityLogEntity entity);
}
