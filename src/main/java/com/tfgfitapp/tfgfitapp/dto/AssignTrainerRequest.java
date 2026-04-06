package com.tfgfitapp.tfgfitapp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO para asignar un entrenador a un cliente.
 * Solo disponible para ADMIN.
 */
@Data
public class AssignTrainerRequest {

    @NotNull(message = "El ID del entrenador es obligatorio")
    private Long trainerId;
}

