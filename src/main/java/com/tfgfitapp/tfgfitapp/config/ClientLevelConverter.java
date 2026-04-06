package com.tfgfitapp.tfgfitapp.config;

import com.tfgfitapp.tfgfitapp.enumeration.ClientLevel;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Convierte ClientLevel entre Java (BEGINNER) y la BD (beginner).
 */
@Converter(autoApply = true)
public class ClientLevelConverter implements AttributeConverter<ClientLevel, String> {

    @Override
    public String convertToDatabaseColumn(ClientLevel attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public ClientLevel convertToEntityAttribute(String dbData) {
        return dbData == null ? null : ClientLevel.valueOf(dbData.toUpperCase());
    }
}

