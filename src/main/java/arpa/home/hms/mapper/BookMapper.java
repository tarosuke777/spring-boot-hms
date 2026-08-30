package arpa.home.hms.mapper;

import arpa.home.hms.entity.BookEntity;
import arpa.home.hms.form.BookForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookMapper {

  @IgnoreAuditFields
  @Mapping(target = "author", ignore = true)
  BookEntity toEntity(BookForm form);

  @Mapping(target = "authorId", ignore = true)
  BookForm toForm(BookEntity entity);

  BookEntity copy(BookEntity entity);

  @IgnoreAuditFields
  @Mapping(target = "author", ignore = true)
  void updateEntityFromForm(BookForm form, @MappingTarget BookEntity entity);
}
