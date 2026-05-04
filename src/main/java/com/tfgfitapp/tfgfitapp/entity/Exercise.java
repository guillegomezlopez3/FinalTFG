package com.tfgfitapp.tfgfitapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 * Ejercicio individual dentro de un WorkoutDay.
 * reps se define como String para soportar rangos como "8-12" o "AMRAP".
 */
@Entity
@Table(name = "exercises")
public class Exercise {

    public Exercise() {}

    public Exercise(Long id, WorkoutDay workoutDay, String name, Integer sets, String reps, Integer restSeconds, Integer durationMinutes, String notes) {
        this.id = id;
        this.workoutDay = workoutDay;
        this.name = name;
        this.sets = sets;
        this.reps = reps;
        this.restSeconds = restSeconds;
        this.durationMinutes = durationMinutes;
        this.notes = notes;
    }

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public WorkoutDay getWorkoutDay() { return workoutDay; }
    public void setWorkoutDay(WorkoutDay workoutDay) { this.workoutDay = workoutDay; }
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

