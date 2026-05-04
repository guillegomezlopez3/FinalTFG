package com.tfgfitapp.tfgfitapp.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para crear o actualizar un ejercicio dentro de un día de entrenamiento.
 */
public class ExerciseRequest {

    public ExerciseRequest() {}

    @NotBlank(message = "El nombre del ejercicio es obligatorio")
    private String name;

    private Integer sets;
    private String reps;
    private Integer restSeconds;
    private Integer durationMinutes;
    private String notes;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getSets() { return sets; }
    public void setSets(Integer sets) { this.sets = sets; }
    public String getReps() { return reps; }
    public void setReps(String reps) { this.reps = reps; }
    public Integer getRestSeconds() { return restSeconds; }
    public void setRestSeconds(Integer restSeconds) { this.restSeconds = restSeconds; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}

