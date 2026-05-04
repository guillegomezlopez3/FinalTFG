package com.tfgfitapp.tfgfitapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * DTO para crear o actualizar una dieta.
 * El trainer se obtiene del usuario autenticado.
 */
public class DietRequest {

    public DietRequest() {}

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clientId;

    @NotBlank(message = "El título es obligatorio")
    private String title;

    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean active;

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}

