package com.tfgfitapp.tfgfitapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO para crear o actualizar un ejercicio dentro de un día de entrenamiento.
 */
@Data
public class ExerciseRequest {

    @NotBlank(message = "El nombre del ejercicio es obligatorio")
    private String name;

    private Integer sets;
    private String reps;
    private Integer restSeconds;
    private Integer durationMinutes;
    private String notes;
}

