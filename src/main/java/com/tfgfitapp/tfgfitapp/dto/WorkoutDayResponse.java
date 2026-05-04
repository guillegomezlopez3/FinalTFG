package com.tfgfitapp.tfgfitapp.dto;

import com.tfgfitapp.tfgfitapp.enumeration.DayOfWeekPlan;

import java.util.List;

/**
 * DTO de respuesta para un día de entrenamiento.
 * Incluye los ejercicios embebidos.
 */
public class WorkoutDayResponse {

    public WorkoutDayResponse() {}

    private Long id;
    private Long workoutPlanId;
    private DayOfWeekPlan dayOfWeek;
    private String focus;
    private String notes;
    private List<ExerciseResponse> exercises;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getWorkoutPlanId() { return workoutPlanId; }
    public void setWorkoutPlanId(Long workoutPlanId) { this.workoutPlanId = workoutPlanId; }
    public DayOfWeekPlan getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(DayOfWeekPlan dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public String getFocus() { return focus; }
    public void setFocus(String focus) { this.focus = focus; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public List<ExerciseResponse> getExercises() { return exercises; }
    public void setExercises(List<ExerciseResponse> exercises) { this.exercises = exercises; }
}
