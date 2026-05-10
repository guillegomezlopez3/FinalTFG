package com.tfgfitapp.tfgfitapp.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad que registra el progreso histórico de un ejercicio para un cliente.
 * 
 * Almacena el peso levantado y las repeticiones logradas en una fecha específica
 * para un {@link PredefinedExercise} determinado.
 */
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
    @JoinColumn(name = "predefined_exercise_id", nullable = true)
    private PredefinedExercise predefinedExercise;

    @Column(nullable = false, length = 150)
    private String exerciseName;

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false)
    private Integer reps;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public ExerciseProgress() {}

    public ExerciseProgress(Client client, PredefinedExercise predefinedExercise, String exerciseName, Double weight, Integer reps, LocalDate date) {
        this.client = client;
        this.predefinedExercise = predefinedExercise;
        this.exerciseName = exerciseName;
        this.weight = weight;
        this.reps = reps;
        this.date = date;
    }

    /** @return El identificador único del registro de progreso. */
    public Long getId() { return id; }
    /** @param id El nuevo ID a asignar. */
    public void setId(Long id) { this.id = id; }
    /** @return El cliente asociado. */
    public Client getClient() { return client; }
    /** @param client El cliente a asignar. */
    public void setClient(Client client) { this.client = client; }
    /** @return El ejercicio predefinido realizado (si aplica). */
    public PredefinedExercise getPredefinedExercise() { return predefinedExercise; }
    /** @param predefinedExercise El ejercicio a asignar. */
    public void setPredefinedExercise(PredefinedExercise predefinedExercise) { this.predefinedExercise = predefinedExercise; }
    /** @return Nombre del ejercicio. */
    public String getExerciseName() { return exerciseName; }
    /** @param exerciseName El nombre del ejercicio. */
    public void setExerciseName(String exerciseName) { this.exerciseName = exerciseName; }
    /** @return El peso levantado (en kg). */
    public Double getWeight() { return weight; }
    /** @param weight El nuevo peso. */
    public void setWeight(Double weight) { this.weight = weight; }
    /** @return El número de repeticiones realizadas. */
    public Integer getReps() { return reps; }
    /** @param reps El nuevo número de repeticiones. */
    public void setReps(Integer reps) { this.reps = reps; }
    /** @return Fecha en la que se realizó el ejercicio. */
    public LocalDate getDate() { return date; }
    /** @param date La fecha del registro. */
    public void setDate(LocalDate date) { this.date = date; }
    /** @return Fecha y hora de creación del registro en el sistema. */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /** @param createdAt La nueva fecha de creación. */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

