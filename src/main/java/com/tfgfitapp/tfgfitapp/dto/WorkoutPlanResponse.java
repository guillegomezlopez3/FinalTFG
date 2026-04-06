package com.tfgfitapp.tfgfitapp.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de respuesta para un plan de entrenamiento.
 * Incluye los días de entrenamiento embebidos.
 */
@Data
@Builder
public class WorkoutPlanResponse {

    private Long id;
    private Long clientId;
    private String clientName;
    private Long trainerId;
    private String trainerName;

    private String title;
    private String objective;
    private String notes;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<WorkoutDayResponse> workoutDays;
}

