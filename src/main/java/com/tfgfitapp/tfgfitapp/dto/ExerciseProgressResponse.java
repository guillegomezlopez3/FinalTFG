package com.tfgfitapp.tfgfitapp.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para el historial de progreso en un ejercicio.
 */
public class ExerciseProgressResponse {
    private Long id;
    private Long clientId;
    private Long predefinedExerciseId;
    private String exerciseName;
    private Double weight;
    private Integer reps;
    private LocalDate date;
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    public Long getPredefinedExerciseId() { return predefinedExerciseId; }
    public void setPredefinedExerciseId(Long predefinedExerciseId) { this.predefinedExerciseId = predefinedExerciseId; }
    public String getExerciseName() { return exerciseName; }
    public void setExerciseName(String exerciseName) { this.exerciseName = exerciseName; }
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
    public Integer getReps() { return reps; }
    public void setReps(Integer reps) { this.reps = reps; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
