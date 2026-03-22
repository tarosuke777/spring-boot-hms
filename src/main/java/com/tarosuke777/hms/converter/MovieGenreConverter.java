package com.tarosuke777.hms.converter;

import com.tarosuke777.hms.enums.MovieGenre;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MovieGenreConverter implements AttributeConverter<MovieGenre, Integer> {
    @Override
    public Integer convertToDatabaseColumn(MovieGenre genre) {
        return (genre == null) ? null : genre.getCode();
    }

    @Override
    public MovieGenre convertToEntityAttribute(Integer value) {
        return (value == null) ? null : MovieGenre.fromValue(value);
    }
}
