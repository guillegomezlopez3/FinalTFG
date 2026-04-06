package com.tfgfitapp.tfgfitapp.config;

import com.tfgfitapp.tfgfitapp.enumeration.DayOfWeekPlan;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Convierte DayOfWeekPlan entre Java (MONDAY) y la BD (monday).
 */
@Converter(autoApply = true)
public class DayOfWeekPlanConverter implements AttributeConverter<DayOfWeekPlan, String> {

    @Override
    public String convertToDatabaseColumn(DayOfWeekPlan attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public DayOfWeekPlan convertToEntityAttribute(String dbData) {
        return dbData == null ? null : DayOfWeekPlan.valueOf(dbData.toUpperCase());
    }
}

