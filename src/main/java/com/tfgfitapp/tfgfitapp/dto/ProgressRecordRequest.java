package com.tfgfitapp.tfgfitapp.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para registrar o actualizar medidas antropométricas de progreso.
 * 
 * Permite al cliente realizar un seguimiento de su evolución física.
 * Todos los campos de medidas son opcionales.
 */
public class ProgressRecordRequest {

    public ProgressRecordRequest() {}

    @NotNull(message = "La fecha del registro es obligatoria")
    private LocalDate recordDate;

    private BigDecimal weight;
    private BigDecimal bodyFat;
    private BigDecimal chest;
    private BigDecimal waist;
    private BigDecimal hips;
    private BigDecimal arms;
    private BigDecimal legs;
    private String notes;
    private Long clientId; // ID del cliente al que se le asigna el progreso (usado por trainers)

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
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
}

