package com.tarosuke777.hms.mapper;

import com.tarosuke777.hms.entity.GoogleCalendarEntity;
import com.tarosuke777.hms.form.GoogleCalendarForm;
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
