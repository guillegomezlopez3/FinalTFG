package com.tfgfitapp.tfgfitapp.dto;

import com.tfgfitapp.tfgfitapp.enumeration.DayOfWeekPlan;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO para crear o actualizar un día dentro de un plan de entrenamiento.
 */
@Data
public class WorkoutDayRequest {

    @NotNull(message = "El día de la semana es obligatorio")
    private DayOfWeekPlan dayOfWeek;

    private String focus;
    private String notes;
}

