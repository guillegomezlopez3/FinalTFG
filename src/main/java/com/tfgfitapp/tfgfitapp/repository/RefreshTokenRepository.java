package com.tfgfitapp.tfgfitapp.repository;

import com.tfgfitapp.tfgfitapp.entity.RefreshToken;
import com.tfgfitapp.tfgfitapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link com.tfgfitapp.tfgfitapp.entity.RefreshToken}.
 * 
 * Gestiona el almacenamiento y eliminación de los tokens de refresco de sesión.
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    /**
     * Busca un token de refresco por su valor.
     * 
     * @param token Valor del token.
     * @return El objeto RefreshToken si existe.
     */
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    @Transactional
    int deleteByUser(User user);
}
