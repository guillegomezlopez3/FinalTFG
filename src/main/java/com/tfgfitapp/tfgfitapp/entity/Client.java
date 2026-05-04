package com.tfgfitapp.tfgfitapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tfgfitapp.tfgfitapp.enumeration.ClientLevel;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Cliente de un entrenador. Vinculado a un User con role CLIENT.
 * Un Client pertenece a un Trainer (puede quedar null hasta ser asignado).
 * Tiene dietas, planes de entrenamiento y registros de progreso asociados.
 *
 * NOTA: trainer_id es NOT NULL en el script SQL, pero en esta fase del proyecto
 * se permite null en JPA para poder registrar clientes antes de asignarles un entrenador.
 * Ajusta nullable=false si el flujo de negocio lo requiere desde el inicio.
 */
@Entity
@Table(name = "clients")
public class Client {

    public Client() {}

    public Client(Long id, User user, Trainer trainer, Integer age, String gender, BigDecimal height, BigDecimal weight, String goal, ClientLevel level, String injuries, String allergies, String notes, Boolean active, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.trainer = trainer;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.goal = goal;
        this.level = level;
        this.injuries = injuries;
        this.allergies = allergies;
        this.notes = notes;
        this.active = active;
        this.createdAt = createdAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = true)
    @JsonIgnore
    private Trainer trainer;

    private Integer age;

    @Column(length = 20)
    private String gender;

    @Column(precision = 5, scale = 2)
    private BigDecimal height;

    @Column(precision = 5, scale = 2)
    private BigDecimal weight;

    @Column(length = 150)
    private String goal;

    // No usar @Enumerated aquí: el ClientLevelConverter (autoApply=true) hace la conversión
    @Column(length = 20)
    private ClientLevel level = ClientLevel.BEGINNER;

    @Column(columnDefinition = "TEXT")
    private String injuries;

    @Column(columnDefinition = "TEXT")
    private String allergies;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Relaciones inversas
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Diet> diets = new ArrayList<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<WorkoutPlan> workoutPlans = new ArrayList<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ProgressRecord> progressRecords = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Manual Getters/Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Trainer getTrainer() { return trainer; }
    public void setTrainer(Trainer trainer) { this.trainer = trainer; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public BigDecimal getHeight() { return height; }
    public void setHeight(BigDecimal height) { this.height = height; }
    public BigDecimal getWeight() { return weight; }
    public void setWeight(BigDecimal weight) { this.weight = weight; }
    public String getGoal() { return goal; }
    public void setGoal(String goal) { this.goal = goal; }
    public ClientLevel getLevel() { return level; }
    public void setLevel(ClientLevel level) { this.level = level; }
    public String getInjuries() { return injuries; }
    public void setInjuries(String injuries) { this.injuries = injuries; }
    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client other = (Client) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

