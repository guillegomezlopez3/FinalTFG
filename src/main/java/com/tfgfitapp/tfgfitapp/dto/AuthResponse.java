package com.tfgfitapp.tfgfitapp.dto;

import com.tfgfitapp.tfgfitapp.enumeration.Role;

/**
 * DTO de respuesta para operaciones de autenticación (registro y login).
 * 
 * Proporciona el token JWT para sesiones subsiguientes, información básica del usuario
 * y metadatos sobre el estado de pago o suscripción si aplica.
 */
public class AuthResponse {

    public AuthResponse() {}

    public AuthResponse(String token, Long userId, String name, String email, Role role) {
        this.token = token;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    private String token;
    private Long userId;
    private String name;
    private String email;
    private Role role;
    private boolean requiresPayment;
    private String checkoutUrl;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public boolean isRequiresPayment() { return requiresPayment; }
    public void setRequiresPayment(boolean requiresPayment) { this.requiresPayment = requiresPayment; }
    public String getCheckoutUrl() { return checkoutUrl; }
    public void setCheckoutUrl(String checkoutUrl) { this.checkoutUrl = checkoutUrl; }
}
