package com.tfgfitapp.tfgfitapp.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * DTO para registrar una nueva marca de progreso en un ejercicio.
 * 
 * Recoge el peso levantado y las repeticiones logradas en una fecha específica.
 */
public class ExerciseProgressRequest {
    private Long predefinedExerciseId;
    
    @NotNull(message = "El nombre del ejercicio es obligatorio")
    private String exerciseName;
    
    @NotNull(message = "El peso es obligatorio")
    private Double weight;
    
    @NotNull(message = "Las repeticiones son obligatorias")
    private Integer reps;
    
    @NotNull(message = "La fecha es obligatoria")
    private LocalDate date;

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
}
