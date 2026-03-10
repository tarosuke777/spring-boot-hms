package com.tarosuke777.hms.converter;

import com.tarosuke777.hms.enums.BookGenre;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BookGenreConverter implements AttributeConverter<BookGenre, Integer> {
    @Override
    public Integer convertToDatabaseColumn(BookGenre genre) {
        return (genre == null) ? null : genre.getCode();
    }

    @Override
    public BookGenre convertToEntityAttribute(Integer value) {
        return (value == null) ? null : BookGenre.fromValue(value);
    }
}
