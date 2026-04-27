package com.tfgfitapp.tfgfitapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @NotBlank(message = "Token es requerido")
    private String token;

    @NotBlank(message = "Nueva contraseña es requerida")
    private String newPassword;
}
