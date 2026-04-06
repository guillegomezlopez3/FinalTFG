package com.tfgfitapp.tfgfitapp.dto;

import lombok.Data;

/**
 * DTO para que el TRAINER actualice su propio perfil.
 * Solo se modifican los campos no nulos.
 */
@Data
public class TrainerUpdateRequest {

    private String phone;
    private String specialty;
    private String description;
}

