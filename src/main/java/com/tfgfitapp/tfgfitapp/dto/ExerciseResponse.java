package com.tfgfitapp.tfgfitapp.dto;

/**
 * DTO de respuesta para un ejercicio de un día de entrenamiento.
 */
public class ExerciseResponse {

    public ExerciseResponse() {}

    private Long id;
    private Long workoutDayId;
    private String name;
    private Integer sets;
    private String reps;
    private Integer restSeconds;
    private Integer durationMinutes;
    private String notes;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getWorkoutDayId() { return workoutDayId; }
    public void setWorkoutDayId(Long workoutDayId) { this.workoutDayId = workoutDayId; }
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

