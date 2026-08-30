package arpa.home.hms.mapper;

import arpa.home.hms.entity.GoogleCalendarEntity;
import arpa.home.hms.form.GoogleCalendarForm;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GoogleCalendarMapper {

  @IgnoreAuditFields
  GoogleCalendarEntity toEntity(GoogleCalendarForm form);

  GoogleCalendarForm toForm(GoogleCalendarEntity entity);

  GoogleCalendarEntity copy(GoogleCalendarEntity entity);

  @IgnoreAuditFields
  void updateEntityFromForm(GoogleCalendarForm form, @MappingTarget GoogleCalendarEntity entity);
}
