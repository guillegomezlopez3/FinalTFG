package com.tfgfitapp.tfgfitapp.config;

import com.tfgfitapp.tfgfitapp.enumeration.ClientLevel;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Convertidor de JPA para el enumerado {@link ClientLevel}.
 * 
 * Gestiona la persistencia del nivel del cliente (Principiante, Intermedio, Avanzado)
 * asegurando que se almacene siempre en formato minúsculo en la base de datos.
 */
@Converter(autoApply = true)
public class ClientLevelConverter implements AttributeConverter<ClientLevel, String> {

    /**
     * Convierte el nivel del cliente de Java a su representación en la base de datos (minúsculas).
     * 
     * @param attribute El nivel en Java.
     * @return El nombre del nivel en minúsculas.
     */
    @Override
    public String convertToDatabaseColumn(ClientLevel attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    /**
     * Convierte la cadena de la base de datos al enumerado ClientLevel de Java.
     * 
     * @param dbData El valor almacenado en la base de datos.
     * @return El enumerado ClientLevel correspondiente.
     */
    @Override
    public ClientLevel convertToEntityAttribute(String dbData) {
        return dbData == null ? null : ClientLevel.valueOf(dbData.toUpperCase());
    }
}

