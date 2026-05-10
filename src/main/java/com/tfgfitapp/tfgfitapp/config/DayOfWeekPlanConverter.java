package com.tfgfitapp.tfgfitapp.config;

import com.tfgfitapp.tfgfitapp.enumeration.DayOfWeekPlan;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Convertidor de JPA para el enumerado {@link DayOfWeekPlan}.
 * 
 * Sincroniza la representación de los días de la semana entre la lógica de Java
 * (mayúsculas) y el estándar de almacenamiento en base de datos (minúsculas).
 */
@Converter(autoApply = true)
public class DayOfWeekPlanConverter implements AttributeConverter<DayOfWeekPlan, String> {

    /**
     * Convierte el día de la semana de Java a su representación en la base de datos (minúsculas).
     * 
     * @param attribute El día de la semana en Java.
     * @return El nombre del día en minúsculas.
     */
    @Override
    public String convertToDatabaseColumn(DayOfWeekPlan attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    /**
     * Convierte la cadena de la base de datos al enumerado DayOfWeekPlan de Java.
     * 
     * @param dbData El valor almacenado en la base de datos.
     * @return El enumerado DayOfWeekPlan correspondiente.
     */
    @Override
    public DayOfWeekPlan convertToEntityAttribute(String dbData) {
        return dbData == null ? null : DayOfWeekPlan.valueOf(dbData.toUpperCase());
    }
}

