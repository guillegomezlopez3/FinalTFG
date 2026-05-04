package com.tfgfitapp.tfgfitapp.dto;

import com.tfgfitapp.tfgfitapp.enumeration.DayOfWeekPlan;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para crear o actualizar un día dentro de un plan de entrenamiento.
 */
public class WorkoutDayRequest {

    public WorkoutDayRequest() {}

    @NotNull(message = "El día de la semana es obligatorio")
    private DayOfWeekPlan dayOfWeek;

    private String focus;
    private String notes;

    public DayOfWeekPlan getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(DayOfWeekPlan dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public String getFocus() { return focus; }
    public void setFocus(String focus) { this.focus = focus; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
