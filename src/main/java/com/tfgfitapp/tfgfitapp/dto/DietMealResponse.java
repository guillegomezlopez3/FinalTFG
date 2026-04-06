package com.tfgfitapp.tfgfitapp.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO de respuesta para una comida de una dieta.
 */
@Data
@Builder
public class DietMealResponse {

    private Long id;
    private Long dietId;
    private String mealType;
    private String mealTime;
    private String foods;
    private Integer calories;
    private String notes;
}

