package com.tfgfitapp.tfgfitapp.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de respuesta para una dieta.
 * Incluye las comidas (meals) embebidas para facilitar la lectura en una sola llamada.
 */
@Data
@Builder
public class DietResponse {

    private Long id;
    private Long clientId;
    private String clientName;
    private Long trainerId;
    private String trainerName;

    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<DietMealResponse> meals;
}

