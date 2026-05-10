package com.tfgfitapp.tfgfitapp.dto;

import java.time.LocalDateTime;

/**
 * DTO de respuesta que contiene la información pública y profesional de un entrenador.
 */
public class TrainerResponse {

    public TrainerResponse() {}

    private Long id;
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String specialty;
    private String description;
    private Boolean active;
    private LocalDateTime createdAt;
    private long clientCount;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public long getClientCount() { return clientCount; }
    public void setClientCount(long clientCount) { this.clientCount = clientCount; }
}

