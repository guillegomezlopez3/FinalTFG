package com.tfgfitapp.tfgfitapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa una Dieta en el sistema.
 * 
 * Una dieta es asignada por un {@link Trainer} a un {@link Client}.
 * Contiene una colección de comidas ({@link DietMeal}) y define un periodo
 * de validez (fecha de inicio y fin).
 */
@Entity
@Table(name = "diets")
public class Diet {

    public Diet() {}

    public Diet(Long id, Client client, Trainer trainer, String title, String description, LocalDate startDate, LocalDate endDate, Boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.client = client;
        this.trainer = trainer;
        this.title = title;
        this.description = description;
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

    @Column(columnDefinition = "TEXT")
    private String description;

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

    @OneToMany(mappedBy = "diet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<DietMeal> meals = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Manual Getters/Setters
    /** @return El identificador único de la dieta. */
    public Long getId() { return id; }
    /** @param id El nuevo ID a asignar. */
    public void setId(Long id) { this.id = id; }
    /** @return El cliente asociado. */
    public Client getClient() { return client; }
    /** @param client El cliente a asignar. */
    public void setClient(Client client) { this.client = client; }
    /** @return El entrenador que creó la dieta. */
    public Trainer getTrainer() { return trainer; }
    /** @param trainer El entrenador a asignar. */
    public void setTrainer(Trainer trainer) { this.trainer = trainer; }
    /** @return El título de la dieta. */
    public String getTitle() { return title; }
    /** @param title El nuevo título. */
    public void setTitle(String title) { this.title = title; }
    /** @return Descripción de la dieta. */
    public String getDescription() { return description; }
    /** @param description La nueva descripción. */
    public void setDescription(String description) { this.description = description; }
    /** @return Fecha de inicio de la dieta. */
    public LocalDate getStartDate() { return startDate; }
    /** @param startDate La nueva fecha de inicio. */
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    /** @return Fecha de fin de la dieta. */
    public LocalDate getEndDate() { return endDate; }
    /** @param endDate La nueva fecha de fin. */
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    /** @return true si la dieta está activa. */
    public Boolean getActive() { return active; }
    /** @param active El nuevo estado. */
    public void setActive(Boolean active) { this.active = active; }
    /** @return Fecha de creación. */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /** @param createdAt La nueva fecha de creación. */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    /** @return Fecha de última actualización. */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    /** @param updatedAt La nueva fecha de actualización. */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    /** @return Lista de comidas incluidas en la dieta. */
    public List<DietMeal> getMeals() { return meals; }
    /** @param meals La nueva lista de comidas. */
    public void setMeals(List<DietMeal> meals) { this.meals = meals; }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

