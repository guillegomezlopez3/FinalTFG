package com.tfgfitapp.tfgfitapp.controller;

import com.tfgfitapp.tfgfitapp.dto.*;
import com.tfgfitapp.tfgfitapp.entity.User;
import com.tfgfitapp.tfgfitapp.service.DietService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador de gestión de dietas y comidas.
 *
 * Endpoints de dietas:
 * - POST   /api/diets                      → TRAINER: crea dieta para uno de sus clientes
 * - GET    /api/diets/client/{clientId}    → TRAINER (propio) / CLIENT (suyas) / ADMIN
 * - GET    /api/diets/{id}                 → TRAINER (propia) / CLIENT (suya) / ADMIN
 * - PUT    /api/diets/{id}                 → TRAINER (propia) / ADMIN
 * - DELETE /api/diets/{id}                 → TRAINER (propia) / ADMIN
 *
 * Endpoints de comidas (sub-recurso):
 * - POST   /api/diets/{id}/meals           → TRAINER
 * - PUT    /api/diets/meals/{mealId}       → TRAINER
 * - DELETE /api/diets/meals/{mealId}       → TRAINER
 */
@RestController
@RequestMapping("/api/diets")
@RequiredArgsConstructor
public class DietController {

    private final DietService dietService;

    // ===== DIETAS =====

    @PostMapping
    @PreAuthorize("hasAnyRole('TRAINER', 'ADMIN')")
    public ResponseEntity<DietResponse> createDiet(
            @Valid @RequestBody DietRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(dietService.createDiet(request, currentUser));
    }

    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasAnyRole('TRAINER', 'CLIENT', 'ADMIN')")
    public ResponseEntity<List<DietResponse>> getDietsByClient(
            @PathVariable Long clientId,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(dietService.getDietsByClient(clientId, currentUser));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('TRAINER', 'CLIENT', 'ADMIN')")
    public ResponseEntity<DietResponse> getDietById(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(dietService.getDietById(id, currentUser));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TRAINER', 'ADMIN')")
    public ResponseEntity<DietResponse> updateDiet(
            @PathVariable Long id,
            @RequestBody DietRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(dietService.updateDiet(id, request, currentUser));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('TRAINER', 'ADMIN')")
    public ResponseEntity<Void> deleteDiet(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        dietService.deleteDiet(id, currentUser);
        return ResponseEntity.noContent().build();
    }

    // ===== COMIDAS (DietMeal) =====

    @PostMapping("/{dietId}/meals")
    @PreAuthorize("hasAnyRole('TRAINER', 'ADMIN')")
    public ResponseEntity<DietMealResponse> addMeal(
            @PathVariable Long dietId,
            @Valid @RequestBody DietMealRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(dietService.addMeal(dietId, request, currentUser));
    }

    @PutMapping("/meals/{mealId}")
    @PreAuthorize("hasAnyRole('TRAINER', 'ADMIN')")
    public ResponseEntity<DietMealResponse> updateMeal(
            @PathVariable Long mealId,
            @RequestBody DietMealRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(dietService.updateMeal(mealId, request, currentUser));
    }

    @DeleteMapping("/meals/{mealId}")
    @PreAuthorize("hasAnyRole('TRAINER', 'ADMIN')")
    public ResponseEntity<Void> deleteMeal(
            @PathVariable Long mealId,
            @AuthenticationPrincipal User currentUser) {
        dietService.deleteMeal(mealId, currentUser);
        return ResponseEntity.noContent().build();
    }
}

