package arpa.home.hms.mapper;

import arpa.home.hms.entity.DiaryEntity;
import arpa.home.hms.form.DiaryForm;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DiaryMapper {

  @IgnoreAuditFields
  DiaryEntity toEntity(DiaryForm form);

  DiaryForm toForm(DiaryEntity entity);

  DiaryEntity copy(DiaryEntity entity);

  @IgnoreAuditFields
  void updateEntityFromForm(DiaryForm form, @MappingTarget DiaryEntity entity);
}
