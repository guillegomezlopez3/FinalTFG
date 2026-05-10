package com.tfgfitapp.tfgfitapp.dto;

import com.tfgfitapp.tfgfitapp.enumeration.Role;

import java.time.LocalDateTime;

/**
 * DTO de respuesta que consolida la información del perfil del usuario.
 * 
 * Incluye datos generales de la cuenta y campos específicos extendidos
 * dependiendo de si el usuario es un Entrenador o un Cliente.
 */
public class UserProfileResponse {

    public UserProfileResponse() {}

    private Long id;
    private String name;
    private String email;
    private Role role;
    private Boolean active;
    private LocalDateTime createdAt;
    private String avatar;

    // Datos del Trainer (solo si role == TRAINER)
    private Long trainerId;
    private String phone;
    private String specialty;
    private String description;
    private Boolean subscriptionActive;

    // Datos del Client (solo si role == CLIENT)
    private Long clientId;
    private Long assignedTrainerId;
    private String assignedTrainerName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public Long getTrainerId() { return trainerId; }
    public void setTrainerId(Long trainerId) { this.trainerId = trainerId; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    public Long getAssignedTrainerId() { return assignedTrainerId; }
    public void setAssignedTrainerId(Long assignedTrainerId) { this.assignedTrainerId = assignedTrainerId; }
    public String getAssignedTrainerName() { return assignedTrainerName; }
    public void setAssignedTrainerName(String assignedTrainerName) { this.assignedTrainerName = assignedTrainerName; }
    public Boolean getSubscriptionActive() { return subscriptionActive; }
    public void setSubscriptionActive(Boolean subscriptionActive) { this.subscriptionActive = subscriptionActive; }
}
