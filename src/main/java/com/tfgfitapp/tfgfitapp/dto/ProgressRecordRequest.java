package com.tfgfitapp.tfgfitapp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para crear o actualizar un registro de progreso.
 * Todos los campos de medidas son opcionales; solo la fecha es obligatoria.
 */
@Data
public class ProgressRecordRequest {

    @NotNull(message = "La fecha del registro es obligatoria")
    private LocalDate recordDate;

    private BigDecimal weight;
    private BigDecimal bodyFat;
    private BigDecimal chest;
    private BigDecimal waist;
    private BigDecimal hips;
    private BigDecimal arms;
    private BigDecimal legs;
    private String notes;
}

