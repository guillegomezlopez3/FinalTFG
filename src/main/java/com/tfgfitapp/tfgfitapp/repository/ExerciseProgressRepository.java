package com.tfgfitapp.tfgfitapp.repository;

import com.tfgfitapp.tfgfitapp.entity.ExerciseProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad {@link com.tfgfitapp.tfgfitapp.entity.ExerciseProgress}.
 * 
 * Gestiona el almacenamiento de marcas históricas de peso y repeticiones
 * para el seguimiento del progreso del cliente.
 */
@Repository
public interface ExerciseProgressRepository extends JpaRepository<ExerciseProgress, Long> {
    /**
     * Recupera el historial de progreso de un ejercicio específico para un cliente.
     * 
     * @param clientId ID del cliente.
     * @param predefinedExerciseId ID del ejercicio del catálogo.
     * @return Lista de registros de progreso ordenados por fecha descendente.
     */
    List<ExerciseProgress> findAllByClientIdAndPredefinedExerciseIdOrderByDateDesc(Long clientId, Long predefinedExerciseId);
    List<ExerciseProgress> findAllByClientIdAndExerciseNameOrderByDateDesc(Long clientId, String exerciseName);
    List<ExerciseProgress> findAllByClientIdOrderByDateDesc(Long clientId);
}
