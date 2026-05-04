package com.tfgfitapp.tfgfitapp.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para crear o actualizar una comida dentro de una dieta.
 */
public class DietMealRequest {

    public DietMealRequest() {}

    @NotBlank(message = "El tipo de comida es obligatorio")
    private String mealType;

    private String mealTime;

    @NotBlank(message = "Los alimentos son obligatorios")
    private String foods;

    private Integer calories;
    private String notes;

    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }
    public String getMealTime() { return mealTime; }
    public void setMealTime(String mealTime) { this.mealTime = mealTime; }
    public String getFoods() { return foods; }
    public void setFoods(String foods) { this.foods = foods; }
    public Integer getCalories() { return calories; }
    public void setCalories(Integer calories) { this.calories = calories; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}

