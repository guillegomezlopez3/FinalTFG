package com.tfgfitapp.tfgfitapp.dto;

/**
 * DTO de respuesta para los detalles de una comida individual.
 */
public class DietMealResponse {

    public DietMealResponse() {}

    private Long id;
    private Long dietId;
    private String mealType;
    private String mealTime;
    private String foods;
    private Integer calories;
    private String notes;
    private Double protein;
    private Double carbs;
    private Double fats;
    private Boolean completed;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getDietId() { return dietId; }
    public void setDietId(Long dietId) { this.dietId = dietId; }
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

