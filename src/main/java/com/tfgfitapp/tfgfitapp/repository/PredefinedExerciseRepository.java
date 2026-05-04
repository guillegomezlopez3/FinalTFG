package com.tfgfitapp.tfgfitapp.repository;

import com.tfgfitapp.tfgfitapp.entity.PredefinedExercise;
import com.tfgfitapp.tfgfitapp.enumeration.MuscleGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredefinedExerciseRepository extends JpaRepository<PredefinedExercise, Long> {
    List<PredefinedExercise> findByMuscleGroup(MuscleGroup muscleGroup);
}
