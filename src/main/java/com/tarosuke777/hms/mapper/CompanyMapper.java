package com.tarosuke777.hms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import com.tarosuke777.hms.entity.CompanyEntity;
import com.tarosuke777.hms.form.CompanyForm;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    @IgnoreAuditFields
    CompanyEntity toEntity(CompanyForm form);

    CompanyForm toForm(CompanyEntity entity);

    CompanyEntity copy(CompanyEntity entity);

    @IgnoreAuditFields
    void updateEntityFromForm(CompanyForm form, @MappingTarget CompanyEntity entity);
}
