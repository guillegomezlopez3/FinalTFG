package com.tfgfitapp.tfgfitapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 * Comida individual dentro de una dieta.
 * mealType: desayuno, almuerzo, cena, snack, etc.
 * mealTime: hora aproximada (e.g. "08:00").
 * foods: descripción libre de los alimentos.
 */
@Entity
@Table(name = "diet_meals")
public class DietMeal {

    public DietMeal() {}

    public DietMeal(Long id, Diet diet, String mealType, String mealTime, String foods, Integer calories, String notes) {
        this.id = id;
        this.diet = diet;
        this.mealType = mealType;
        this.mealTime = mealTime;
        this.foods = foods;
        this.calories = calories;
        this.notes = notes;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diet_id", nullable = false)
    @JsonIgnore
    private Diet diet;

    @Column(name = "meal_type", nullable = false, length = 50)
    private String mealType;

    @Column(name = "meal_time", length = 20)
    private String mealTime;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String foods;

    private Integer calories;

    @Column(columnDefinition = "TEXT")
    private String notes;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Diet getDiet() { return diet; }
    public void setDiet(Diet diet) { this.diet = diet; }
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

