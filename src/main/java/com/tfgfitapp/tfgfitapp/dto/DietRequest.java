package com.tfgfitapp.tfgfitapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO para crear o actualizar una dieta.
 * El trainer se obtiene del usuario autenticado.
 */
@Data
public class DietRequest {

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clientId;

    @NotBlank(message = "El título es obligatorio")
    private String title;

    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean active;
}

