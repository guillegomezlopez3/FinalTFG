package com.tfgfitapp.tfgfitapp.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "exercise_progress")
public class ExerciseProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "predefined_exercise_id", nullable = false)
    private PredefinedExercise predefinedExercise;

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false)
    private Integer reps;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public ExerciseProgress() {}

    public ExerciseProgress(Client client, PredefinedExercise predefinedExercise, Double weight, Integer reps, LocalDate date) {
        this.client = client;
        this.predefinedExercise = predefinedExercise;
        this.weight = weight;
        this.reps = reps;
        this.date = date;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    public PredefinedExercise getPredefinedExercise() { return predefinedExercise; }
    public void setPredefinedExercise(PredefinedExercise predefinedExercise) { this.predefinedExercise = predefinedExercise; }
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
    public Integer getReps() { return reps; }
    public void setReps(Integer reps) { this.reps = reps; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

