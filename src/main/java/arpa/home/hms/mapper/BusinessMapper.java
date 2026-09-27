package arpa.home.hms.mapper;

import arpa.home.hms.entity.BusinessEntity;
import arpa.home.hms.form.BusinessForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BusinessMapper {

  @IgnoreAuditFields
  @Mapping(target = "canvas", ignore = true)
  BusinessEntity toEntity(BusinessForm form);

  @Mapping(target = "id", source = "id")
  @Mapping(target = "name", source = "name")
  @Mapping(target = "genre", source = "genre")
  @Mapping(target = "status", source = "status")
  @Mapping(target = "overview", source = "overview")
  @Mapping(target = "customerSegments",
      expression = "java(entity.getCanvas() != null ? entity.getCanvas().getCustomerSegments() : null)")
  @Mapping(target = "valueProposition",
      expression = "java(entity.getCanvas() != null ? entity.getCanvas().getValueProposition() : null)")
  @Mapping(target = "channels",
      expression = "java(entity.getCanvas() != null ? entity.getCanvas().getChannels() : null)")
  @Mapping(target = "customerRelationships",
      expression = "java(entity.getCanvas() != null ? entity.getCanvas().getCustomerRelationships() : null)")
  @Mapping(target = "revenueStreams",
      expression = "java(entity.getCanvas() != null ? entity.getCanvas().getRevenueStreams() : null)")
  @Mapping(target = "keyResources",
      expression = "java(entity.getCanvas() != null ? entity.getCanvas().getKeyResources() : null)")
  @Mapping(target = "keyActivities",
      expression = "java(entity.getCanvas() != null ? entity.getCanvas().getKeyActivities() : null)")
  @Mapping(target = "keyPartners",
      expression = "java(entity.getCanvas() != null ? entity.getCanvas().getKeyPartners() : null)")
  @Mapping(target = "costStructure",
      expression = "java(entity.getCanvas() != null ? entity.getCanvas().getCostStructure() : null)")
  BusinessForm toForm(BusinessEntity entity);

  BusinessEntity copy(BusinessEntity entity);

  @IgnoreAuditFields
  @Mapping(target = "canvas", ignore = true)
  void updateEntityFromForm(BusinessForm form, @MappingTarget BusinessEntity entity);
}
