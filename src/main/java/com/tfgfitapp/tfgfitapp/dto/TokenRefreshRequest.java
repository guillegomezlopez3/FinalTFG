package com.tfgfitapp.tfgfitapp.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para solicitar la renovación del token de acceso.
 * 
 * Contiene el refresh token persistido anteriormente para obtener un nuevo par de tokens.
 */
public class TokenRefreshRequest {

    public TokenRefreshRequest() {}

    public TokenRefreshRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    @NotBlank(message = "Refresh Token es requerido")
    private String refreshToken;

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}
