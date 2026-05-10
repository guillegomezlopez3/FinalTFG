package com.tfgfitapp.tfgfitapp.controller;

import com.tfgfitapp.tfgfitapp.dto.*;
import com.tfgfitapp.tfgfitapp.entity.User;
import com.tfgfitapp.tfgfitapp.service.DietService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para la gestión de Dietas y Comidas.
 * 
 * Permite a los entrenadores crear y modificar dietas para sus clientes,
 * y a los clientes consultar sus dietas asignadas y marcar comidas como completadas.
 */
@RestController
@RequestMapping("/api/diets")
public class DietController {

    public DietController(DietService dietService) {
        this.dietService = dietService;
    }

    private final DietService dietService;

    // ===== DIETAS =====

    /**
     * Crea una nueva dieta para un cliente.
     * 
     * @param request Datos de la dieta.
     * @param currentUser Usuario que realiza la creación (entrenador).
     * @return 201 Created con la dieta creada.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('TRAINER', 'ADMIN')")
    public ResponseEntity<DietResponse> createDiet(
            @Valid @RequestBody DietRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(dietService.createDiet(request, currentUser));
    }

    /**
     * Obtiene todas las dietas de un cliente específico.
     * 
     * @param clientId Identificador del cliente.
     * @param currentUser Usuario que realiza la consulta.
     * @return Lista de dietas encontradas.
     */
    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasAnyRole('TRAINER', 'CLIENT', 'ADMIN')")
    public ResponseEntity<List<DietResponse>> getDietsByClient(
            @PathVariable Long clientId,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(dietService.getDietsByClient(clientId, currentUser));
    }

    /**
     * Obtiene los detalles de una dieta por su ID.
     * 
     * @param id Identificador de la dieta.
     * @param currentUser Usuario que realiza la consulta.
     * @return La dieta encontrada.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('TRAINER', 'CLIENT', 'ADMIN')")
    public ResponseEntity<DietResponse> getDietById(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(dietService.getDietById(id, currentUser));
    }

    /**
     * Actualiza una dieta existente.
     * 
     * @param id Identificador de la dieta.
     * @param request Datos actualizados.
     * @param currentUser Usuario que realiza la actualización.
     * @return La dieta actualizada.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TRAINER', 'ADMIN')")
    public ResponseEntity<DietResponse> updateDiet(
            @PathVariable Long id,
            @RequestBody DietRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(dietService.updateDiet(id, request, currentUser));
    }

    /**
     * Elimina una dieta por su ID.
     * 
     * @param id Identificador de la dieta.
     * @param currentUser Usuario que realiza la eliminación.
     * @return 204 No Content.
     */
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

    @PatchMapping("/meals/{mealId}/toggle")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<DietMealResponse> toggleMealCompletion(
            @PathVariable Long mealId,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(dietService.toggleMealCompletion(mealId, currentUser));
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

