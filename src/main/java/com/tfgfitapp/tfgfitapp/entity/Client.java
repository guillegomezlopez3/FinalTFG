package com.tfgfitapp.tfgfitapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tfgfitapp.tfgfitapp.enumeration.ClientLevel;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa a un Cliente en el sistema.
 * 
 * Un cliente está vinculado a un {@link User} con rol CLIENT y pertenece a un {@link Trainer}.
 * Contiene información antropométrica, objetivos, lesiones, alergias y el historial
 * de sus dietas, planes de entrenamiento y registros de progreso.
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

    /** Indica si el cliente tiene una suscripción mensual activa (pago al entrenador). */
    @Column(name = "subscription_active", nullable = false)
    private Boolean subscriptionActive = false;

    /** ID del cliente en Stripe (para futuras integraciones con Checkout dinámico). */
    @Column(name = "stripe_customer_id", length = 100)
    private String stripeCustomerId;

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
    /** @return El identificador único del cliente. */
    public Long getId() { return id; }
    /** @param id El nuevo ID a asignar. */
    public void setId(Long id) { this.id = id; }
    /** @return El usuario base asociado. */
    public User getUser() { return user; }
    /** @param user El nuevo usuario a asociar. */
    public void setUser(User user) { this.user = user; }
    /** @return El entrenador asignado. */
    public Trainer getTrainer() { return trainer; }
    /** @param trainer El nuevo entrenador a asignar. */
    public void setTrainer(Trainer trainer) { this.trainer = trainer; }
    /** @return La edad del cliente. */
    public Integer getAge() { return age; }
    /** @param age La nueva edad. */
    public void setAge(Integer age) { this.age = age; }
    /** @return El género del cliente. */
    public String getGender() { return gender; }
    /** @param gender El nuevo género. */
    public void setGender(String gender) { this.gender = gender; }
    /** @return La altura en cm/m. */
    public BigDecimal getHeight() { return height; }
    /** @param height La nueva altura. */
    public void setHeight(BigDecimal height) { this.height = height; }
    /** @return El peso en kg. */
    public BigDecimal getWeight() { return weight; }
    /** @param weight El nuevo peso. */
    public void setWeight(BigDecimal weight) { this.weight = weight; }
    /** @return El objetivo fitness. */
    public String getGoal() { return goal; }
    /** @param goal El nuevo objetivo. */
    public void setGoal(String goal) { this.goal = goal; }
    /** @return El nivel de experiencia. */
    public ClientLevel getLevel() { return level; }
    /** @param level El nuevo nivel. */
    public void setLevel(ClientLevel level) { this.level = level; }
    /** @return Información sobre lesiones. */
    public String getInjuries() { return injuries; }
    /** @param injuries Las lesiones registradas. */
    public void setInjuries(String injuries) { this.injuries = injuries; }
    /** @return Información sobre alergias. */
    public String getAllergies() { return allergies; }
    /** @param allergies Las alergias registradas. */
    public void setAllergies(String allergies) { this.allergies = allergies; }
    /** @return Notas adicionales. */
    public String getNotes() { return notes; }
    /** @param notes Las nuevas notas. */
    public void setNotes(String notes) { this.notes = notes; }
    /** @return true si el cliente está activo. */
    public Boolean getActive() { return active; }
    /** @param active El nuevo estado. */
    public void setActive(Boolean active) { this.active = active; }
    /** @return true si la suscripción mensual está activa. */
    public Boolean getSubscriptionActive() { return subscriptionActive; }
    /** @param subscriptionActive El nuevo estado de suscripción. */
    public void setSubscriptionActive(Boolean subscriptionActive) { this.subscriptionActive = subscriptionActive; }
    /** @return ID del cliente en Stripe. */
    public String getStripeCustomerId() { return stripeCustomerId; }
    /** @param stripeCustomerId El ID de Stripe. */
    public void setStripeCustomerId(String stripeCustomerId) { this.stripeCustomerId = stripeCustomerId; }
    /** @return Fecha de creación del registro. */
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

