package com.tfgfitapp.tfgfitapp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Servicio encargado de generar, validar y extraer información de los JWT.
 *
 * Algoritmo: HS256 con clave secreta configurada en application.properties.
 * La clave debe tener al menos 256 bits (32 caracteres ASCII).
 */
@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String secretKey;

    @Value("${app.jwt.expiration}")
    private long jwtExpiration;

    // ===== GENERACIÓN =====

    /**
     * Genera un token JWT simple para un usuario.
     * 
     * @param userDetails Detalles del usuario autenticado.
     * @return Cadena del token JWT.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Genera un token JWT incluyendo claims adicionales personalizados.
     * 
     * @param extraClaims Mapa de claims extra para incluir en el payload.
     * @param userDetails Detalles del usuario autenticado.
     * @return Cadena del token JWT.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    // ===== VALIDACIÓN =====

    /**
     * Valida si un token pertenece al usuario y no ha expirado.
     * 
     * @param token Token JWT a validar.
     * @param userDetails Detalles del usuario contra los que validar.
     * @return true si es válido, false en caso contrario.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // ===== EXTRACCIÓN DE CLAIMS =====

    /**
     * Extrae el nombre de usuario (subject) contenido en el token.
     * 
     * @param token Token JWT.
     * @return El email del usuario.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrae un claim específico utilizando un resolver de claims.
     * 
     * @param <T> Tipo del dato a extraer.
     * @param token Token JWT.
     * @param claimsResolver Función para extraer el dato deseado.
     * @return El valor del claim extraído.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

