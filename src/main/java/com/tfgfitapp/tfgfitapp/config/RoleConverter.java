package com.tfgfitapp.tfgfitapp.config;

import com.tfgfitapp.tfgfitapp.enumeration.Role;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Convertidor de JPA para el enumerado {@link Role}.
 * 
 * Transforma el valor del enumerado a minúsculas para su almacenamiento en la base de datos
 * y lo convierte de vuelta a mayúsculas al leerlo, facilitando la consistencia.
 */
@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, String> {

    /**
     * Convierte el rol de Java a su representación en la base de datos (minúsculas).
     * 
     * @param attribute El rol en Java.
     * @return El nombre del rol en minúsculas.
     */
    @Override
    public String convertToDatabaseColumn(Role attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    /**
     * Convierte la cadena de la base de datos al enumerado Role de Java.
     * 
     * @param dbData El valor almacenado en la base de datos.
     * @return El enumerado Role correspondiente.
     */
    @Override
    public Role convertToEntityAttribute(String dbData) {
        return dbData == null ? null : Role.valueOf(dbData.toUpperCase());
    }
}

