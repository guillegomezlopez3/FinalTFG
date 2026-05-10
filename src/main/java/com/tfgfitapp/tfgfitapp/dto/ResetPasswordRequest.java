package com.tfgfitapp.tfgfitapp.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para completar el restablecimiento de la contraseña utilizando un token.
 * 
 * Contiene el token de validación y la nueva contraseña deseada por el usuario.
 */
public class ResetPasswordRequest {
    @NotBlank(message = "Token es requerido")
    private String token;

    @NotBlank(message = "Nueva contraseña es requerida")
    private String newPassword;

    public ResetPasswordRequest() {}

    public ResetPasswordRequest(String token, String newPassword) {
        this.token = token;
        this.newPassword = newPassword;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
