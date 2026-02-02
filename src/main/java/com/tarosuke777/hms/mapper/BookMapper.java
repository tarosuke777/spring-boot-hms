package com.tarosuke777.hms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import com.tarosuke777.hms.entity.BookEntity;
import com.tarosuke777.hms.form.BookForm;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @IgnoreAuditFields
    BookEntity toEntity(BookForm form);

    BookForm toForm(BookEntity entity);

    BookEntity copy(BookEntity entity);

    @IgnoreAuditFields
    void updateEntityFromForm(BookForm form, @MappingTarget BookEntity entity);

}
