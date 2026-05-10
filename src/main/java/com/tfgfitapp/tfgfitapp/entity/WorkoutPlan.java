package com.tfgfitapp.tfgfitapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Plan de entrenamiento asignado por un Trainer a un Client.
 * Contiene múltiples WorkoutDays (días de entrenamiento).
 */
@Entity
@Table(name = "workout_plans")
public class WorkoutPlan {

    public WorkoutPlan() {}

    public WorkoutPlan(Long id, Client client, Trainer trainer, String title, String objective, String notes, LocalDate startDate, LocalDate endDate, Boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.client = client;
        this.trainer = trainer;
        this.title = title;
        this.objective = objective;
        this.notes = notes;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @JsonIgnore
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    @JsonIgnore
    private Trainer trainer;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 200)
    private String objective;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "workoutPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<WorkoutDay> workoutDays = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Manual Getters/Setters
    /** @return El identificador único del plan. */
    public Long getId() { return id; }
    /** @param id El nuevo ID a asignar. */
    public void setId(Long id) { this.id = id; }
    /** @return El cliente dueño del plan. */
    public Client getClient() { return client; }
    /** @param client El cliente a asociar. */
    public void setClient(Client client) { this.client = client; }
    /** @return El entrenador que diseñó el plan. */
    public Trainer getTrainer() { return trainer; }
    /** @param trainer El entrenador a asociar. */
    public void setTrainer(Trainer trainer) { this.trainer = trainer; }
    /** @return Título descriptivo del plan. */
    public String getTitle() { return title; }
    /** @param title El nuevo título. */
    public void setTitle(String title) { this.title = title; }
    /** @return Objetivo principal del plan (e.g., "Pérdida de grasa"). */
    public String getObjective() { return objective; }
    /** @param objective El nuevo objetivo. */
    public void setObjective(String objective) { this.objective = objective; }
    /** @return Recomendaciones o notas generales. */
    public String getNotes() { return notes; }
    /** @param notes Las nuevas notas. */
    public void setNotes(String notes) { this.notes = notes; }
    /** @return Fecha de inicio de vigencia. */
    public LocalDate getStartDate() { return startDate; }
    /** @param startDate La nueva fecha de inicio. */
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    /** @return Fecha de fin de vigencia. */
    public LocalDate getEndDate() { return endDate; }
    /** @param endDate La nueva fecha de fin. */
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    /** @return true si el plan está activo actualmente. */
    public Boolean getActive() { return active; }
    /** @param active El nuevo estado de activación. */
    public void setActive(Boolean active) { this.active = active; }
    /** @return Fecha de creación del plan. */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /** @param createdAt La nueva fecha de creación. */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    /** @return Fecha de la última modificación. */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    /** @param updatedAt La nueva fecha de actualización. */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    /** @return Lista de días que componen la rutina semanal. */
    public List<WorkoutDay> getWorkoutDays() { return workoutDays; }
    /** @param workoutDays La nueva lista de días. */
    public void setWorkoutDays(List<WorkoutDay> workoutDays) { this.workoutDays = workoutDays; }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

