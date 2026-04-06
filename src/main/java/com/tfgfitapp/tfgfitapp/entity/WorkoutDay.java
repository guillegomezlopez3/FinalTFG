package com.tfgfitapp.tfgfitapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tfgfitapp.tfgfitapp.enumeration.DayOfWeekPlan;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Día de entrenamiento dentro de un WorkoutPlan.
 * Cada día tiene un listado de Exercise (ejercicios).
 */
@Entity
@Table(name = "workout_days")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutDay {

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
    @Builder.Default
    private List<Exercise> exercises = new ArrayList<>();
}

