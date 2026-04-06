package com.tfgfitapp.tfgfitapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

/**
 * Comida individual dentro de una dieta.
 * mealType: desayuno, almuerzo, cena, snack, etc.
 * mealTime: hora aproximada (e.g. "08:00").
 * foods: descripción libre de los alimentos.
 */
@Entity
@Table(name = "diet_meals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DietMeal {

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
}

