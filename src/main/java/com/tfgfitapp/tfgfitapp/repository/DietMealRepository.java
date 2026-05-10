package com.tfgfitapp.tfgfitapp.repository;

import com.tfgfitapp.tfgfitapp.entity.DietMeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad {@link DietMeal}.
 * 
 * Gestiona el acceso a las comidas individuales contenidas en una dieta.
 */
@Repository
public interface DietMealRepository extends JpaRepository<DietMeal, Long> {

    /**
     * Busca todas las comidas asociadas a una dieta específica.
     * 
     * @param dietId ID de la dieta.
     * @return Lista de comidas pertenecientes a la dieta.
     */
    List<DietMeal> findAllByDietId(Long dietId);
}
