package arpa.home.hms.mapper;

import arpa.home.hms.entity.BookReadingLogEntity;
import arpa.home.hms.form.BookReadingLogForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookReadingLogMapper {

  @IgnoreAuditFields
  @Mapping(target = "book", ignore = true)
  BookReadingLogEntity toEntity(BookReadingLogForm form);

  @Mapping(target = "bookId", ignore = true)
  BookReadingLogForm toForm(BookReadingLogEntity entity);

  BookReadingLogEntity copy(BookReadingLogEntity entity);

  @IgnoreAuditFields
  @Mapping(target = "book", ignore = true)
  void updateEntityFromForm(BookReadingLogForm form, @MappingTarget BookReadingLogEntity entity);
}
