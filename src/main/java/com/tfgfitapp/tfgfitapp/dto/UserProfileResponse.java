package com.tfgfitapp.tfgfitapp.dto;

import com.tfgfitapp.tfgfitapp.enumeration.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para el endpoint GET /api/me.
 * Devuelve los datos del usuario autenticado junto con
 * datos de su perfil (trainer o client) si corresponde.
 */
@Data
@Builder
public class UserProfileResponse {

    private Long id;
    private String name;
    private String email;
    private Role role;
    private Boolean active;
    private LocalDateTime createdAt;

    // Datos del Trainer (solo si role == TRAINER)
    private Long trainerId;
    private String phone;
    private String specialty;
    private String description;

    // Datos del Client (solo si role == CLIENT)
    private Long clientId;
    private Long assignedTrainerId;
    private String assignedTrainerName;
}

