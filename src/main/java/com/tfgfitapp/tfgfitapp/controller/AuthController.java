package com.tfgfitapp.tfgfitapp.controller;

import com.tfgfitapp.tfgfitapp.dto.AuthResponse;
import com.tfgfitapp.tfgfitapp.dto.LoginRequest;
import com.tfgfitapp.tfgfitapp.dto.RegisterRequest;
import com.tfgfitapp.tfgfitapp.service.AuthService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

/**
 * Controlador de autenticación.
 * Proporciona endpoints públicos para el registro y el inicio de sesión.
 * 
 * Gestiona el flujo de entrada de datos para:
 * - Registro de nuevos entrenadores y clientes.
 * - Autenticación de usuarios existentes mediante JWT.
 * - Confirmación de correo electrónico.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    public AuthController(AuthService authService, com.tfgfitapp.tfgfitapp.service.EmailService emailService) {
        this.authService = authService;
        this.emailService = emailService;
    }

    private final AuthService authService;
    private final com.tfgfitapp.tfgfitapp.service.EmailService emailService;

    /**
     * Registra un nuevo usuario con rol TRAINER o CLIENT.
     * 
     * @param request Datos de registro (nombre, email, password, etc.)
     * @return 201 Created con el token JWT y datos básicos del usuario registrado.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Autentica un usuario existente mediante sus credenciales.
     * 
     * @param request Datos de login (email y password).
     * @return 200 OK con el token JWT si la autenticación es correcta.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para confirmar el email de un usuario recién registrado.
     * 
     * @param token Token de confirmación recibido por email.
     * @return Redirección a la página de resultado de la confirmación.
     */
    @GetMapping("/confirm")
    public ResponseEntity<Void> confirmEmail(@RequestParam("token") String token) {
        boolean success = emailService.confirmEmail(token);
        String redirectUrl = success ? "/confirm-email?success=true" : "/confirm-email?error=true";
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(redirectUrl))
                .build();
    }
}

