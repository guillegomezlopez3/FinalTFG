package com.tfgfitapp.tfgfitapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO para crear o actualizar una comida dentro de una dieta.
 */
@Data
public class DietMealRequest {

    @NotBlank(message = "El tipo de comida es obligatorio")
    private String mealType;

    private String mealTime;

    @NotBlank(message = "Los alimentos son obligatorios")
    private String foods;

    private Integer calories;
    private String notes;
}

