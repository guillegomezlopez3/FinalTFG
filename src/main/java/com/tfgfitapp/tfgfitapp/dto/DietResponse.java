package com.tfgfitapp.tfgfitapp.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de respuesta para un plan nutricional (dieta) completo.
 * 
 * Incluye la lista de comidas asociadas para permitir una visualización
 * integral del plan en una sola respuesta.
 */
public class DietResponse {

    public DietResponse() {}

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }
    public Long getTrainerId() { return trainerId; }
    public void setTrainerId(Long trainerId) { this.trainerId = trainerId; }
    public String getTrainerName() { return trainerName; }
    public void setTrainerName(String trainerName) { this.trainerName = trainerName; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public List<DietMealResponse> getMeals() { return meals; }
    public void setMeals(List<DietMealResponse> meals) { this.meals = meals; }
}

