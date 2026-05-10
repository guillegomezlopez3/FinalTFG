package com.tfgfitapp.tfgfitapp.repository;

import com.tfgfitapp.tfgfitapp.entity.Diet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link Diet}.
 * 
 * Proporciona métodos para recuperar planes nutricionales asociados a clientes
 * o entrenadores, y realizar verificaciones de propiedad.
 */
@Repository
public interface DietRepository extends JpaRepository<Diet, Long> {

    List<Diet> findAllByClientId(Long clientId);

    List<Diet> findAllByTrainerId(Long trainerId);

    /**
     * Busca una dieta por su ID y el ID del entrenador que la creó.
     * 
     * @param id ID de la dieta.
     * @param trainerId ID del entrenador.
     * @return Un Optional con la dieta si existe y pertenece al entrenador.
     */
    Optional<Diet> findByIdAndTrainerId(Long id, Long trainerId);

    /**
     * Busca una dieta por su ID y el ID del cliente al que pertenece.
     * 
     * @param id ID de la dieta.
     * @param clientId ID del cliente.
     * @return Un Optional con la dieta si existe y pertenece al cliente.
     */
    Optional<Diet> findByIdAndClientId(Long id, Long clientId);

    boolean existsByIdAndTrainerId(Long id, Long trainerId);
    
    long countByActive(boolean active);
}
