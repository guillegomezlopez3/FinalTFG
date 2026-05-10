package com.tfgfitapp.tfgfitapp.repository;

import com.tfgfitapp.tfgfitapp.entity.EmailConfirmationToken;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link com.tfgfitapp.tfgfitapp.entity.EmailConfirmationToken}.
 * 
 * Utilizado para la persistencia y recuperación de los tokens de confirmación de email.
 */
@Repository
public interface EmailConfirmationTokenRepository extends JpaRepository<EmailConfirmationToken, Long> {
    /**
     * Busca un token por su valor de cadena.
     * 
     * @param token Valor del token.
     * @return El token de confirmación si existe.
     */
    Optional<EmailConfirmationToken> findByToken(String token);
}
