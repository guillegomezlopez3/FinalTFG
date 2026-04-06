package com.tfgfitapp.tfgfitapp.dto;

import com.tfgfitapp.tfgfitapp.enumeration.ClientLevel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO para actualizar el perfil de un cliente.
 * El trainer y el user no se modifican desde aquí.
 * Todos los campos son opcionales (se actualizan solo los no nulos).
 */
@Data
public class ClientUpdateRequest {

    private Integer age;
    private String gender;
    private BigDecimal height;
    private BigDecimal weight;
    private String goal;
    private ClientLevel level;
    private String injuries;
    private String allergies;
    private String notes;
    private Boolean active;
}

