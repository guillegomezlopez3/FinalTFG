package com.tfgfitapp.tfgfitapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordRequest {
    @NotBlank(message = "Email es requerido")
    private String email;
}
