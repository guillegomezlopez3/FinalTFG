package com.tfgfitapp.tfgfitapp.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para un entrenador.
 * Incluye datos del User asociado y numero de clientes asignados.
 */
@Data
@Builder
public class TrainerResponse {

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
}

