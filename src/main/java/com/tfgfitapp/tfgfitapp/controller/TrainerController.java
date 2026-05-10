package com.tfgfitapp.tfgfitapp.controller;

import com.tfgfitapp.tfgfitapp.dto.PageResponse;
import com.tfgfitapp.tfgfitapp.dto.TrainerResponse;
import com.tfgfitapp.tfgfitapp.dto.TrainerUpdateRequest;
import com.tfgfitapp.tfgfitapp.entity.User;
import com.tfgfitapp.tfgfitapp.service.TrainerService;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para la gestión de perfiles de Entrenadores.
 * 
 * Proporciona endpoints para que los entrenadores consulten y actualicen su perfil,
 * para que los administradores listen y consulten todos los entrenadores.
 */
@RestController
@RequestMapping("/api/trainers")
public class TrainerController {

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    private final TrainerService trainerService;

    /**
     * Obtiene el perfil del entrenador autenticado.
     * 
     * @param currentUser Entrenador autenticado.
     * @return 200 OK con los detalles del perfil.
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<TrainerResponse> getMyProfile(
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(trainerService.getMyProfile(currentUser));
    }

    /**
     * Actualiza el perfil del entrenador autenticado.
     * 
     * @param request Datos del perfil actualizados.
     * @param currentUser Entrenador autenticado.
     * @return 200 OK con el perfil actualizado.
     */
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

