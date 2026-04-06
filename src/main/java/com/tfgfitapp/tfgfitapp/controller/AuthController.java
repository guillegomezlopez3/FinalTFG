package com.tfgfitapp.tfgfitapp.controller;

import com.tfgfitapp.tfgfitapp.dto.AuthResponse;
import com.tfgfitapp.tfgfitapp.dto.LoginRequest;
import com.tfgfitapp.tfgfitapp.dto.RegisterRequest;
import com.tfgfitapp.tfgfitapp.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador de autenticación.
 * Endpoints públicos que no requieren JWT.
 *
 * POST /api/auth/register -> Registro de TRAINER o CLIENT
 * POST /api/auth/login    -> Login con email y password
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Registra un nuevo usuario con rol TRAINER o CLIENT.
     * Devuelve 201 Created con el token JWT y datos básicos del usuario.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Autentica un usuario existente.
     * Devuelve 200 OK con el token JWT y datos básicos del usuario.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}

