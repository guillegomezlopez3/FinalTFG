package com.tfgfitapp.tfgfitapp.repository;

import com.tfgfitapp.tfgfitapp.entity.Trainer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link com.tfgfitapp.tfgfitapp.entity.Trainer}.
 * 
 * Gestiona el acceso a los perfiles de entrenadores y su asociación con clientes de Stripe.
 */
@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    /**
     * Busca el perfil del entrenador por su identificador de usuario base.
     * 
     * @param userId ID del usuario.
     * @return El perfil del entrenador con el usuario cargado.
     */
    @EntityGraph(attributePaths = {"user"})
    Optional<Trainer> findByUserId(Long userId);

    @EntityGraph(attributePaths = {"user"})
    Optional<Trainer> findByStripeCustomerId(String stripeCustomerId);
}

