package com.tfgfitapp.tfgfitapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad que representa un Registro de Progreso físico de un cliente.
 * 
 * Almacena diversas medidas antropométricas (peso, grasa corporal, perímetros)
 * en una fecha determinada para realizar un seguimiento de la evolución del cliente.
 */
@Entity
@Table(name = "progress_records")
public class ProgressRecord {

    public ProgressRecord() {}

    public ProgressRecord(Long id, Client client, LocalDate recordDate, BigDecimal weight, BigDecimal bodyFat, BigDecimal chest, BigDecimal waist, BigDecimal hips, BigDecimal arms, BigDecimal legs, String notes, LocalDateTime createdAt) {
        this.id = id;
        this.client = client;
        this.recordDate = recordDate;
        this.weight = weight;
        this.bodyFat = bodyFat;
        this.chest = chest;
        this.waist = waist;
        this.hips = hips;
        this.arms = arms;
        this.legs = legs;
        this.notes = notes;
        this.createdAt = createdAt;
    }

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

    /** @return El identificador único del registro. */
    public Long getId() { return id; }
    /** @param id El nuevo ID a asignar. */
    public void setId(Long id) { this.id = id; }
    /** @return El cliente asociado al registro. */
    public Client getClient() { return client; }
    /** @param client El cliente a asignar. */
    public void setClient(Client client) { this.client = client; }
    /** @return Fecha en la que se tomaron las medidas. */
    public LocalDate getRecordDate() { return recordDate; }
    /** @param recordDate La fecha del registro. */
    public void setRecordDate(LocalDate recordDate) { this.recordDate = recordDate; }
    /** @return Peso corporal en kg. */
    public BigDecimal getWeight() { return weight; }
    /** @param weight El nuevo peso. */
    public void setWeight(BigDecimal weight) { this.weight = weight; }
    /** @return Porcentaje de grasa corporal. */
    public BigDecimal getBodyFat() { return bodyFat; }
    /** @param bodyFat El nuevo porcentaje de grasa. */
    public void setBodyFat(BigDecimal bodyFat) { this.bodyFat = bodyFat; }
    /** @return Perímetro del pecho en cm. */
    public BigDecimal getChest() { return chest; }
    /** @param chest La nueva medida de pecho. */
    public void setChest(BigDecimal chest) { this.chest = chest; }
    /** @return Perímetro de la cintura en cm. */
    public BigDecimal getWaist() { return waist; }
    /** @param waist La nueva medida de cintura. */
    public void setWaist(BigDecimal waist) { this.waist = waist; }
    /** @return Perímetro de las caderas en cm. */
    public BigDecimal getHips() { return hips; }
    /** @param hips La nueva medida de caderas. */
    public void setHips(BigDecimal hips) { this.hips = hips; }
    /** @return Perímetro de los brazos en cm. */
    public BigDecimal getArms() { return arms; }
    /** @param arms La nueva medida de brazos. */
    public void setArms(BigDecimal arms) { this.arms = arms; }
    /** @return Perímetro de las piernas en cm. */
    public BigDecimal getLegs() { return legs; }
    /** @param legs La nueva medida de piernas. */
    public void setLegs(BigDecimal legs) { this.legs = legs; }
    /** @return Notas u observaciones adicionales. */
    public String getNotes() { return notes; }
    /** @param notes Las nuevas notas. */
    public void setNotes(String notes) { this.notes = notes; }
    /** @return Fecha de creación del registro. */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /** @param createdAt La nueva fecha de creación. */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

