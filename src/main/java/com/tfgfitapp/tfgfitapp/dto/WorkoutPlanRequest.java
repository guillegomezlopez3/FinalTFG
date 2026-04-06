package com.tfgfitapp.tfgfitapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO para crear o actualizar un plan de entrenamiento.
 * El trainer se obtiene del usuario autenticado.
 */
@Data
public class WorkoutPlanRequest {

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clientId;

    @NotBlank(message = "El título es obligatorio")
    private String title;

    private String objective;
    private String notes;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean active;
}

