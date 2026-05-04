package com.tfgfitapp.tfgfitapp.controller;

import com.tfgfitapp.tfgfitapp.dto.UserProfileResponse;
import com.tfgfitapp.tfgfitapp.entity.User;
import com.tfgfitapp.tfgfitapp.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import com.tfgfitapp.tfgfitapp.dto.ChangePasswordRequest;

/**
 * Controlador para operaciones del usuario autenticado.
 * Todos los endpoints requieren JWT válido (configurado en SecurityConfig).
 */
@RestController
@RequestMapping("/api")
public class UserController {

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private final UserService userService;

    /**
     * GET /api/me
     * Devuelve el perfil completo del usuario autenticado.
     * Incluye datos del Trainer o Client según el rol.
     *
     * Ejemplo de uso:
     * Authorization: Bearer <token>
     */
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMyProfile(
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(userService.getMyProfile(currentUser));
    }

    /**
     * PUT /api/me/password
     * Permite al usuario autenticado cambiar su contraseña.
     */
    @PutMapping("/me/password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(currentUser, request);
        return ResponseEntity.ok().build();
    }
}

