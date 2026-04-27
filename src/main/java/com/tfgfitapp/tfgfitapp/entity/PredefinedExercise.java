package com.tfgfitapp.tfgfitapp.entity;

import jakarta.persistence.*;
import lombok.*;
import com.tfgfitapp.tfgfitapp.enumeration.MuscleGroup;

@Entity
@Table(name = "predefined_exercises")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PredefinedExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MuscleGroup muscleGroup;

    @Column(length = 100)
    private String equipment;
}
