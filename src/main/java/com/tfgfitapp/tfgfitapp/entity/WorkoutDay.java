package com.tfgfitapp.tfgfitapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tfgfitapp.tfgfitapp.enumeration.DayOfWeekPlan;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Día de entrenamiento dentro de un WorkoutPlan.
 * Cada día tiene un listado de Exercise (ejercicios).
 */
@Entity
@Table(name = "workout_days")
public class WorkoutDay {

    public WorkoutDay() {}

    public WorkoutDay(Long id, WorkoutPlan workoutPlan, DayOfWeekPlan dayOfWeek, String focus, String notes) {
        this.id = id;
        this.workoutPlan = workoutPlan;
        this.dayOfWeek = dayOfWeek;
        this.focus = focus;
        this.notes = notes;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_plan_id", nullable = false)
    @JsonIgnore
    private WorkoutPlan workoutPlan;

    // No usar @Enumerated aquí: el DayOfWeekPlanConverter (autoApply=true) hace la conversión
    @Column(name = "day_of_week", nullable = false, length = 15)
    private DayOfWeekPlan dayOfWeek;

    @Column(length = 100)
    private String focus;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "workoutDay", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Exercise> exercises = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public WorkoutPlan getWorkoutPlan() { return workoutPlan; }
    public void setWorkoutPlan(WorkoutPlan workoutPlan) { this.workoutPlan = workoutPlan; }
    public DayOfWeekPlan getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(DayOfWeekPlan dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public String getFocus() { return focus; }
    public void setFocus(String focus) { this.focus = focus; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public List<Exercise> getExercises() { return exercises; }
    public void setExercises(List<Exercise> exercises) { this.exercises = exercises; }
}

