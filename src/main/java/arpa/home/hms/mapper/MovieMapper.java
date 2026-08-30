package arpa.home.hms.mapper;

import arpa.home.hms.entity.MovieEntity;
import arpa.home.hms.form.MovieForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MovieMapper {

  @IgnoreAuditFields
  @Mapping(target = "cast", ignore = true)
  MovieEntity toEntity(MovieForm form);

  @Mapping(target = "castId", ignore = true)
  MovieForm toForm(MovieEntity entity);

  MovieEntity copy(MovieEntity entity);

  @IgnoreAuditFields
  @Mapping(target = "cast", ignore = true)
  void updateEntityFromForm(MovieForm form, @MappingTarget MovieEntity entity);
}
