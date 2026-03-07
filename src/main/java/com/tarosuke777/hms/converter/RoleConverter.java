package com.tarosuke777.hms.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.tarosuke777.hms.enums.Role;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Role role) {
        return (role == null) ? null : role.getValue();
    }

    @Override
    public Role convertToEntityAttribute(Integer value) {
        return (value == null) ? null : Role.fromValue(value);
    }
}
