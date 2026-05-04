package com.tfgfitapp.tfgfitapp.entity;

import jakarta.persistence.*;
import com.tfgfitapp.tfgfitapp.enumeration.MuscleGroup;

@Entity
@Table(name = "predefined_exercises")
public class PredefinedExercise {

    public PredefinedExercise() {}

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
    public PredefinedExercise(Long id, String name, MuscleGroup muscleGroup, String equipment) {
        this.id = id;
        this.name = name;
        this.muscleGroup = muscleGroup;
        this.equipment = equipment;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public MuscleGroup getMuscleGroup() { return muscleGroup; }
    public void setMuscleGroup(MuscleGroup muscleGroup) { this.muscleGroup = muscleGroup; }
    public String getEquipment() { return equipment; }
    public void setEquipment(String equipment) { this.equipment = equipment; }
}
