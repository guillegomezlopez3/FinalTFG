package com.tfgfitapp.tfgfitapp.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO para la solicitud de asignación de un entrenador a un cliente específico.
 * 
 * Utilizado por administradores para vincular perfiles de clientes con sus preparadores.
 */
public class AssignTrainerRequest {

    public AssignTrainerRequest() {}

    public AssignTrainerRequest(Long trainerId) {
        this.trainerId = trainerId;
    }

    @NotNull(message = "El ID del entrenador es obligatorio")
    private Long trainerId;

    public Long getTrainerId() { return trainerId; }
    public void setTrainerId(Long trainerId) { this.trainerId = trainerId; }
}
