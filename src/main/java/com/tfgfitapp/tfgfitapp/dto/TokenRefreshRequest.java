package com.tfgfitapp.tfgfitapp.dto;

import jakarta.validation.constraints.NotBlank;

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
