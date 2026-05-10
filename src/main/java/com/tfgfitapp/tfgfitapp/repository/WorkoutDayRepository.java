package com.tfgfitapp.tfgfitapp.repository;

import com.tfgfitapp.tfgfitapp.entity.WorkoutDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad {@link com.tfgfitapp.tfgfitapp.entity.WorkoutDay}.
 * 
 * Gestiona el acceso a los días configurados dentro de un plan de entrenamiento.
 */
@Repository
public interface WorkoutDayRepository extends JpaRepository<WorkoutDay, Long> {

    /**
     * Recupera todos los días de entrenamiento asociados a un plan específico.
     * 
     * @param workoutPlanId ID del plan de entrenamiento.
     * @return Lista de días del plan.
     */
    List<WorkoutDay> findAllByWorkoutPlanId(Long workoutPlanId);
}

