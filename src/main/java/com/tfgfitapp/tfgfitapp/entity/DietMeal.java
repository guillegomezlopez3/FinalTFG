package com.tfgfitapp.tfgfitapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 * Entidad que representa una Comida individual dentro de una Dieta.
 * 
 * Contiene información sobre el tipo de comida (desayuno, cena, etc.),
 * el horario, los alimentos que la componen y su información nutricional.
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

    private Double protein;
    private Double carbs;
    private Double fats;

    private Boolean completed;

    /** @return El identificador único de la comida. */
    public Long getId() { return id; }
    /** @param id El nuevo ID a asignar. */
    public void setId(Long id) { this.id = id; }
    /** @return La dieta a la que pertenece esta comida. */
    public Diet getDiet() { return diet; }
    /** @param diet La dieta a asignar. */
    public void setDiet(Diet diet) { this.diet = diet; }
    /** @return El tipo de comida (e.g., Desayuno). */
    public String getMealType() { return mealType; }
    /** @param mealType El nuevo tipo de comida. */
    public void setMealType(String mealType) { this.mealType = mealType; }
    /** @return La hora programada para la comida. */
    public String getMealTime() { return mealTime; }
    /** @param mealTime La nueva hora. */
    public void setMealTime(String mealTime) { this.mealTime = mealTime; }
    /** @return Descripción de los alimentos. */
    public String getFoods() { return foods; }
    /** @param foods Los nuevos alimentos. */
    public void setFoods(String foods) { this.foods = foods; }
    /** @return Calorías totales estimadas. */
    public Integer getCalories() { return calories; }
    /** @param calories Las nuevas calorías. */
    public void setCalories(Integer calories) { this.calories = calories; }
    /** @return Notas adicionales sobre la comida. */
    public String getNotes() { return notes; }
    /** @param notes Las nuevas notas. */
    public void setNotes(String notes) { this.notes = notes; }
    /** @return Gramos de proteína. */
    public Double getProtein() { return protein; }
    /** @param protein La nueva cantidad de proteína. */
    public void setProtein(Double protein) { this.protein = protein; }
    /** @return Gramos de carbohidratos. */
    public Double getCarbs() { return carbs; }
    /** @param carbs La nueva cantidad de carbohidratos. */
    public void setCarbs(Double carbs) { this.carbs = carbs; }
    /** @return Gramos de grasas. */
    public Double getFats() { return fats; }
    /** @param fats La nueva cantidad de grasas. */
    public void setFats(Double fats) { this.fats = fats; }
    /** @return true si el cliente ha marcado la comida como completada. */
    public Boolean getCompleted() { return completed; }
    /** @param completed El nuevo estado de completitud. */
    public void setCompleted(Boolean completed) { this.completed = completed; }
}

