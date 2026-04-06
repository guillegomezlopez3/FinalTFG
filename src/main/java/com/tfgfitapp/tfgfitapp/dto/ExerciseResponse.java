package com.tfgfitapp.tfgfitapp.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO de respuesta para un ejercicio de un día de entrenamiento.
 */
@Data
@Builder
public class ExerciseResponse {

    private Long id;
    private Long workoutDayId;
    private String name;
    private Integer sets;
    private String reps;
    private Integer restSeconds;
    private Integer durationMinutes;
    private String notes;
}

