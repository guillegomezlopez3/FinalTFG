package com.tfgfitapp.tfgfitapp.dto;

/**
 * DTO de respuesta con los detalles de un ejercicio prescrito.
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
    private String gifUrl;
    private Long predefinedExerciseId;

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
    public String getGifUrl() { return gifUrl; }
    public void setGifUrl(String gifUrl) { this.gifUrl = gifUrl; }
    public Long getPredefinedExerciseId() { return predefinedExerciseId; }
    public void setPredefinedExerciseId(Long predefinedExerciseId) { this.predefinedExerciseId = predefinedExerciseId; }
}

