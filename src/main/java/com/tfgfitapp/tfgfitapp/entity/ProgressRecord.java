package com.tfgfitapp.tfgfitapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Registro de progreso físico de un Client en una fecha determinada.
 * Todas las medidas corporales son opcionales para flexibilidad.
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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    public LocalDate getRecordDate() { return recordDate; }
    public void setRecordDate(LocalDate recordDate) { this.recordDate = recordDate; }
    public BigDecimal getWeight() { return weight; }
    public void setWeight(BigDecimal weight) { this.weight = weight; }
    public BigDecimal getBodyFat() { return bodyFat; }
    public void setBodyFat(BigDecimal bodyFat) { this.bodyFat = bodyFat; }
    public BigDecimal getChest() { return chest; }
    public void setChest(BigDecimal chest) { this.chest = chest; }
    public BigDecimal getWaist() { return waist; }
    public void setWaist(BigDecimal waist) { this.waist = waist; }
    public BigDecimal getHips() { return hips; }
    public void setHips(BigDecimal hips) { this.hips = hips; }
    public BigDecimal getArms() { return arms; }
    public void setArms(BigDecimal arms) { this.arms = arms; }
    public BigDecimal getLegs() { return legs; }
    public void setLegs(BigDecimal legs) { this.legs = legs; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

