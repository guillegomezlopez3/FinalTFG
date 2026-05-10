package com.tfgfitapp.tfgfitapp.repository;

import com.tfgfitapp.tfgfitapp.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad {@link com.tfgfitapp.tfgfitapp.entity.Exercise}.
 * 
 * Permite gestionar los ejercicios asignados dentro de un plan de entrenamiento.
 */
@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    /**
     * Recupera todos los ejercicios asignados a un día de entrenamiento concreto.
     * 
     * @param workoutDayId ID del día de entrenamiento.
     * @return Lista de ejercicios del día.
     */
    List<Exercise> findAllByWorkoutDayId(Long workoutDayId);
}

