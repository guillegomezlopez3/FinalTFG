package com.tfgfitapp.tfgfitapp.config;

import com.tfgfitapp.tfgfitapp.enumeration.Role;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Convierte Role entre Java (TRAINER) y la BD (trainer).
 * autoApply = true lo aplica a todos los campos de tipo Role sin necesidad de @Convert.
 */
@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, String> {

    @Override
    public String convertToDatabaseColumn(Role attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public Role convertToEntityAttribute(String dbData) {
        return dbData == null ? null : Role.valueOf(dbData.toUpperCase());
    }
}

