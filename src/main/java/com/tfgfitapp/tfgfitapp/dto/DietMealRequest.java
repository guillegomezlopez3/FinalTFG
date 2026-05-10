package com.tfgfitapp.tfgfitapp.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para la creación o edición de una comida individual dentro de una dieta.
 * 
 * Define el tipo de comida, horario, alimentos sugeridos y su desglose nutricional.
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
    private Double protein;
    private Double carbs;
    private Double fats;
    private Boolean completed;

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
    public Double getProtein() { return protein; }
    public void setProtein(Double protein) { this.protein = protein; }
    public Double getCarbs() { return carbs; }
    public void setCarbs(Double carbs) { this.carbs = carbs; }
    public Double getFats() { return fats; }
    public void setFats(Double fats) { this.fats = fats; }
    public Boolean getCompleted() { return completed; }
    public void setCompleted(Boolean completed) { this.completed = completed; }
}

