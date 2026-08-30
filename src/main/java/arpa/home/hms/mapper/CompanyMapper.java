package arpa.home.hms.mapper;

import arpa.home.hms.entity.CompanyEntity;
import arpa.home.hms.form.CompanyForm;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

  @IgnoreAuditFields
  CompanyEntity toEntity(CompanyForm form);

  CompanyForm toForm(CompanyEntity entity);

  CompanyEntity copy(CompanyEntity entity);

  @IgnoreAuditFields
  void updateEntityFromForm(CompanyForm form, @MappingTarget CompanyEntity entity);
}
