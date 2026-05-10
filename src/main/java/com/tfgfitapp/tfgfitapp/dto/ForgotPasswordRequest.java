package com.tfgfitapp.tfgfitapp.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para la solicitud de recuperación de contraseña.
 * 
 * Inicia el proceso enviando un enlace o token al correo electrónico del usuario.
 */
public class ForgotPasswordRequest {
    @NotBlank(message = "Email es requerido")
    private String email;

    public ForgotPasswordRequest() {}

    public ForgotPasswordRequest(String email) {
        this.email = email;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
