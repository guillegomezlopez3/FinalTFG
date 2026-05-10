package com.tfgfitapp.tfgfitapp.repository;

import com.tfgfitapp.tfgfitapp.entity.PredefinedExercise;
import com.tfgfitapp.tfgfitapp.enumeration.MuscleGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad {@link com.tfgfitapp.tfgfitapp.entity.PredefinedExercise}.
 * 
 * Acceso al catálogo maestro de ejercicios que pueden ser asignados a los planes.
 */
@Repository
public interface PredefinedExerciseRepository extends JpaRepository<PredefinedExercise, Long> {
    /**
     * Filtra los ejercicios del catálogo por grupo muscular.
     * 
     * @param muscleGroup El grupo muscular (ej. PECHO, PIERNAS).
     * @return Lista de ejercicios que trabajan dicho grupo.
     */
    List<PredefinedExercise> findByMuscleGroup(MuscleGroup muscleGroup);
}
