package com.tfgfitapp.tfgfitapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa a un Entrenador en el sistema.
 * 
 * Un entrenador está vinculado a un {@link User} con rol TRAINER.
 * Gestiona sus clientes, dietas y planes de entrenamiento.
 * Incluye campos para la integración con Stripe para la gestión de suscripciones.
 */
@Entity
@Table(name = "trainers")
public class Trainer {

    public Trainer() {}

    public Trainer(Long id, User user, String phone, String specialty, String description, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.phone = phone;
        this.specialty = specialty;
        this.description = description;
        this.createdAt = createdAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonIgnore
    private User user;

    @Column(length = 20)
    private String phone;

    @Column(length = 100)
    private String specialty;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "stripe_customer_id", length = 100)
    private String stripeCustomerId;

    @Column(name = "stripe_subscription_id", length = 100)
    private String stripeSubscriptionId;

    @Column(name = "subscription_active", nullable = false)
    private Boolean subscriptionActive = false;

    @Column(name = "trial_ends_at")
    private LocalDateTime trialEndsAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Relaciones inversas
    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Client> clients = new ArrayList<>();

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Diet> diets = new ArrayList<>();

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<WorkoutPlan> workoutPlans = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Manual Getters/Setters
    /** @return El identificador único del entrenador. */
    public Long getId() { return id; }
    /** @param id El nuevo ID a asignar. */
    public void setId(Long id) { this.id = id; }
    /** @return El usuario base asociado. */
    public User getUser() { return user; }
    /** @param user El nuevo usuario a asociar. */
    public void setUser(User user) { this.user = user; }
    /** @return El teléfono de contacto. */
    public String getPhone() { return phone; }
    /** @param phone El nuevo teléfono. */
    public void setPhone(String phone) { this.phone = phone; }
    /** @return La especialidad del entrenador. */
    public String getSpecialty() { return specialty; }
    /** @param specialty La nueva especialidad. */
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    /** @return Descripción profesional. */
    public String getDescription() { return description; }
    /** @param description La nueva descripción. */
    public void setDescription(String description) { this.description = description; }
    /** @return ID del cliente en Stripe. */
    public String getStripeCustomerId() { return stripeCustomerId; }
    /** @param stripeCustomerId El ID de Stripe. */
    public void setStripeCustomerId(String stripeCustomerId) { this.stripeCustomerId = stripeCustomerId; }
    /** @return ID de la suscripción en Stripe. */
    public String getStripeSubscriptionId() { return stripeSubscriptionId; }
    /** @param stripeSubscriptionId El ID de la suscripción. */
    public void setStripeSubscriptionId(String stripeSubscriptionId) { this.stripeSubscriptionId = stripeSubscriptionId; }
    /** @return true si la suscripción está activa. */
    public Boolean getSubscriptionActive() { return subscriptionActive; }
    /** @param subscriptionActive El nuevo estado de suscripción. */
    public void setSubscriptionActive(Boolean subscriptionActive) { this.subscriptionActive = subscriptionActive; }
    /** @return Fecha de fin del periodo de prueba. */
    public LocalDateTime getTrialEndsAt() { return trialEndsAt; }
    /** @param trialEndsAt La nueva fecha de fin. */
    public void setTrialEndsAt(LocalDateTime trialEndsAt) { this.trialEndsAt = trialEndsAt; }
    /** @return Fecha de registro del entrenador. */
    public LocalDateTime getCreatedAt() { return createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trainer)) return false;
        Trainer other = (Trainer) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

