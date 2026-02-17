package com.tarosuke777.hms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.tarosuke777.hms.entity.BookEntity;
import com.tarosuke777.hms.form.BookForm;

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
