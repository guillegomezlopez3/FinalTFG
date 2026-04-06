package com.tfgfitapp.tfgfitapp.controller;

import com.tfgfitapp.tfgfitapp.dto.PageResponse;
import com.tfgfitapp.tfgfitapp.dto.TrainerResponse;
import com.tfgfitapp.tfgfitapp.dto.TrainerUpdateRequest;
import com.tfgfitapp.tfgfitapp.entity.User;
import com.tfgfitapp.tfgfitapp.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para el perfil del entrenador.
 *
 * Endpoints:
 * - GET /api/trainers/me       → TRAINER: su propio perfil con conteo de clientes
 * - PUT /api/trainers/me       → TRAINER: actualiza su perfil
 * - GET /api/trainers          → ADMIN: lista paginada de todos los entrenadores
 * - GET /api/trainers/{id}     → ADMIN: perfil de un entrenador concreto
 */
@RestController
@RequestMapping("/api/trainers")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<TrainerResponse> getMyProfile(
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(trainerService.getMyProfile(currentUser));
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<TrainerResponse> updateMyProfile(
            @RequestBody TrainerUpdateRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(trainerService.updateMyProfile(request, currentUser));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<TrainerResponse>> getAllTrainers(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(trainerService.getAllTrainers(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TrainerResponse> getTrainerById(@PathVariable Long id) {
        return ResponseEntity.ok(trainerService.getTrainerById(id));
    }
}

