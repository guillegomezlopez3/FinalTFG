package com.tfgfitapp.tfgfitapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

/**
 * Ejercicio individual dentro de un WorkoutDay.
 * reps se define como String para soportar rangos como "8-12" o "AMRAP".
 */
@Entity
@Table(name = "exercises")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_day_id", nullable = false)
    @JsonIgnore
    private WorkoutDay workoutDay;

    @Column(nullable = false, length = 150)
    private String name;

    private Integer sets;

    @Column(length = 50)
    private String reps;

    @Column(name = "rest_seconds")
    private Integer restSeconds;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(columnDefinition = "TEXT")
    private String notes;
}

