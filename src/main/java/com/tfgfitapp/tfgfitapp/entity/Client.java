package com.tfgfitapp.tfgfitapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tfgfitapp.tfgfitapp.enumeration.ClientLevel;
import jakarta.persistence.*;
import lombok.*;

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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

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
    @Builder.Default
    private ClientLevel level = ClientLevel.BEGINNER;

    @Column(columnDefinition = "TEXT")
    private String injuries;

    @Column(columnDefinition = "TEXT")
    private String allergies;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Relaciones inversas
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @Builder.Default
    private List<Diet> diets = new ArrayList<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @Builder.Default
    private List<WorkoutPlan> workoutPlans = new ArrayList<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @Builder.Default
    private List<ProgressRecord> progressRecords = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

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

