package arpa.home.hms.mapper;

import arpa.home.hms.entity.TrainingMenuEntity;
import arpa.home.hms.form.TrainingMenuForm;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TrainingMenuMapper {

  @IgnoreAuditFields
  TrainingMenuEntity toEntity(TrainingMenuForm form);

  TrainingMenuForm toForm(TrainingMenuEntity entity);

  TrainingMenuEntity copy(TrainingMenuEntity entity);

  @IgnoreAuditFields
  void updateEntityFromForm(TrainingMenuForm form, @MappingTarget TrainingMenuEntity entity);
}
