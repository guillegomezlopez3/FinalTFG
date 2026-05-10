package com.tfgfitapp.tfgfitapp.service;

import com.tfgfitapp.tfgfitapp.entity.RefreshToken;
import com.tfgfitapp.tfgfitapp.repository.RefreshTokenRepository;
import com.tfgfitapp.tfgfitapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio para la gestión de tokens de refresco (Refresh Tokens).
 * 
 * Permite la renovación de tokens de acceso JWT sin necesidad de re-autenticación,
 * gestionando la expiración y persistencia de los mismos en la base de datos.
 */
@Service
public class RefreshTokenService {

    @Value("${jwt.refresh.expiration:604800000}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Crea un nuevo token de refresco para un usuario.
     * 
     * @param userId ID del usuario.
     * @return El objeto RefreshToken generado.
     */
    @Transactional
    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    /**
     * Busca un token de refresco por su valor.
     * 
     * @param token El token en formato cadena.
     * @return El token opcional.
     */
    @Transactional(readOnly = true)
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    /**
     * Verifica si un token ha expirado. Si ha expirado, lo elimina de la base de datos.
     * 
     * @param token El token a verificar.
     * @return El token si es válido.
     * @throws RuntimeException si el token ha expirado.
     */
    @Transactional
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    /**
     * Elimina todos los tokens de refresco asociados a un usuario.
     * 
     * @param userId ID del usuario.
     * @return Número de tokens eliminados.
     */
    @Transactional
    public int deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }
}
