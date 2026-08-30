package arpa.home.hms.mapper;

import arpa.home.hms.entity.ArtistEntity;
import arpa.home.hms.form.ArtistForm;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ArtistMapper {

  @IgnoreAuditFields
  ArtistEntity toEntity(ArtistForm form);

  ArtistForm toForm(ArtistEntity entity);

  ArtistEntity copy(ArtistEntity entity);

  @IgnoreAuditFields
  void updateEntityFromForm(ArtistForm form, @MappingTarget ArtistEntity entity);
}
