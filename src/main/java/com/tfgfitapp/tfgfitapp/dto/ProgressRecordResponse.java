package com.tfgfitapp.tfgfitapp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para un registro de progreso físico.
 */
public class ProgressRecordResponse {

    public ProgressRecordResponse() {}

    private Long id;
    private Long clientId;
    private String clientName;

    private LocalDate recordDate;
    private BigDecimal weight;
    private BigDecimal bodyFat;
    private BigDecimal chest;
    private BigDecimal waist;
    private BigDecimal hips;
    private BigDecimal arms;
    private BigDecimal legs;
    private String notes;

    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }
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
}

