package com.tfgfitapp.tfgfitapp.repository;

import com.tfgfitapp.tfgfitapp.entity.WorkoutPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link com.tfgfitapp.tfgfitapp.entity.WorkoutPlan}.
 * 
 * Gestiona la persistencia de planes de entrenamiento completos.
 */
@Repository
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Long> {

    List<WorkoutPlan> findAllByClientId(Long clientId);

    List<WorkoutPlan> findAllByTrainerId(Long trainerId);

    /**
     * Busca un plan por su ID y el ID del entrenador que lo prescribió.
     * 
     * @param id ID del plan.
     * @param trainerId ID del entrenador.
     * @return El plan si coincide con los criterios.
     */
    Optional<WorkoutPlan> findByIdAndTrainerId(Long id, Long trainerId);

    Optional<WorkoutPlan> findByIdAndClientId(Long id, Long clientId);

    boolean existsByIdAndTrainerId(Long id, Long trainerId);
    
    long countByActive(boolean active);
    
    Page<WorkoutPlan> findAllByClientIdOrderByCreatedAtDesc(Long clientId, Pageable pageable);
    
    Page<WorkoutPlan> findAllByClientIdAndActive(Long clientId, Boolean active, Pageable pageable);
}
