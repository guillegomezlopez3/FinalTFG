package com.tfgfitapp.tfgfitapp.repository;

import com.tfgfitapp.tfgfitapp.entity.PasswordResetToken;
import com.tfgfitapp.tfgfitapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link com.tfgfitapp.tfgfitapp.entity.PasswordResetToken}.
 * 
 * Gestiona el ciclo de vida de los tokens utilizados para el restablecimiento de contraseñas.
 */
@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    /**
     * Busca un token de restablecimiento por su valor.
     * 
     * @param token Valor del token.
     * @return El token de restablecimiento si existe.
     */
    Optional<PasswordResetToken> findByToken(String token);

    @Modifying
    @Transactional
    void deleteByUser(User user);
}
