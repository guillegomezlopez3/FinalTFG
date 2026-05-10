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

    /** @return El identificador único del día de entrenamiento. */
    public Long getId() { return id; }
    /** @param id El nuevo ID a asignar. */
    public void setId(Long id) { this.id = id; }
    /** @return El plan de entrenamiento al que pertenece este día. */
    public WorkoutPlan getWorkoutPlan() { return workoutPlan; }
    /** @param workoutPlan El plan de entrenamiento a asociar. */
    public void setWorkoutPlan(WorkoutPlan workoutPlan) { this.workoutPlan = workoutPlan; }
    /** @return Día de la semana (Lunes, Martes, etc.). */
    public DayOfWeekPlan getDayOfWeek() { return dayOfWeek; }
    /** @param dayOfWeek El día de la semana a asignar. */
    public void setDayOfWeek(DayOfWeekPlan dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    /** @return Enfoque del entrenamiento (e.g., "Pecho y Tríceps"). */
    public String getFocus() { return focus; }
    /** @param focus El nuevo enfoque. */
    public void setFocus(String focus) { this.focus = focus; }
    /** @return Notas adicionales para este día. */
    public String getNotes() { return notes; }
    /** @param notes Las nuevas notas. */
    public void setNotes(String notes) { this.notes = notes; }
    /** @return Lista de ejercicios prescritos para este día. */
    public List<Exercise> getExercises() { return exercises; }
    /** @param exercises La nueva lista de ejercicios. */
    public void setExercises(List<Exercise> exercises) { this.exercises = exercises; }
}

