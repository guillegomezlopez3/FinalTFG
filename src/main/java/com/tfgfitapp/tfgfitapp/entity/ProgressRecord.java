package com.tfgfitapp.tfgfitapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Registro de progreso físico de un Client en una fecha determinada.
 * Todas las medidas corporales son opcionales para flexibilidad.
 */
@Entity
@Table(name = "progress_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgressRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @JsonIgnore
    private Client client;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(precision = 5, scale = 2)
    private BigDecimal weight;

    @Column(name = "body_fat", precision = 5, scale = 2)
    private BigDecimal bodyFat;

    @Column(precision = 5, scale = 2)
    private BigDecimal chest;

    @Column(precision = 5, scale = 2)
    private BigDecimal waist;

    @Column(precision = 5, scale = 2)
    private BigDecimal hips;

    @Column(precision = 5, scale = 2)
    private BigDecimal arms;

    @Column(precision = 5, scale = 2)
    private BigDecimal legs;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

