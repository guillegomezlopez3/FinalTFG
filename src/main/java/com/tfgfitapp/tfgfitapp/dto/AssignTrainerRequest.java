package com.tfgfitapp.tfgfitapp.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO para asignar un entrenador a un cliente.
 * Solo disponible para ADMIN.
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
