package com.tfgfitapp.tfgfitapp.dto;

import com.tfgfitapp.tfgfitapp.enumeration.DayOfWeekPlan;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * DTO de respuesta para un día de entrenamiento.
 * Incluye los ejercicios embebidos.
 */
@Data
@Builder
public class WorkoutDayResponse {

    private Long id;
    private Long workoutPlanId;
    private DayOfWeekPlan dayOfWeek;
    private String focus;
    private String notes;

    private List<ExerciseResponse> exercises;
}

