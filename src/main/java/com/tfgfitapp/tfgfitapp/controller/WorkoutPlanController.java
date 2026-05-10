package com.tfgfitapp.tfgfitapp.controller;

import com.tfgfitapp.tfgfitapp.dto.*;
import com.tfgfitapp.tfgfitapp.entity.User;
import com.tfgfitapp.tfgfitapp.service.WorkoutPlanService;
import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para la gestión de Planes de Entrenamiento.
 * 
 * Gestiona la jerarquía completa de entrenamientos: Planes, Días de entrenamiento
 * y Ejercicios individuales dentro de cada día.
 */
@RestController
@RequestMapping("/api/workout-plans")
public class WorkoutPlanController {

    public WorkoutPlanController(WorkoutPlanService workoutPlanService) {
        this.workoutPlanService = workoutPlanService;
    }

    private final WorkoutPlanService workoutPlanService;

    // ===== PLANES =====

    /**
     * Crea un nuevo plan de entrenamiento para un cliente.
     * 
     * @param request Datos del plan.
     * @param currentUser Usuario que crea el plan (entrenador).
     * @return 201 Created con el plan creado.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('TRAINER', 'ADMIN')")
    public ResponseEntity<WorkoutPlanResponse> createPlan(
            @Valid @RequestBody WorkoutPlanRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(workoutPlanService.createPlan(request, currentUser));
    }

    /**
     * Obtiene los planes de entrenamiento de un cliente, permitiendo filtrar por estado.
     * 
     * @param clientId ID del cliente.
     * @param active Opcional: filtrar solo planes activos o inactivos.
     * @param pageable Parámetros de paginación.
     * @param currentUser Usuario que consulta.
     * @return Página de planes de entrenamiento.
     */
    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasAnyRole('TRAINER', 'CLIENT', 'ADMIN')")
    public ResponseEntity<PageResponse<WorkoutPlanResponse>> getPlansByClient(
            @PathVariable Long clientId,
            @RequestParam(required = false) Boolean active,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(workoutPlanService.getPlansByClient(clientId, active, pageable, currentUser));
    }

    /**
     * Obtiene un plan de entrenamiento específico por su ID.
     * 
     * @param id ID del plan.
     * @param currentUser Usuario que consulta.
     * @return El plan detallado (incluyendo días y ejercicios).
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('TRAINER', 'CLIENT', 'ADMIN')")
    public ResponseEntity<WorkoutPlanResponse> getPlanById(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(workoutPlanService.getPlanById(id, currentUser));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TRAINER', 'ADMIN')")
    public ResponseEntity<WorkoutPlanResponse> updatePlan(
            @PathVariable Long id,
            @RequestBody WorkoutPlanRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(workoutPlanService.updatePlan(id, request, currentUser));
    }

    /**
     * Elimina un plan de entrenamiento completo.
     * 
     * @param id ID del plan.
     * @param currentUser Usuario que realiza la eliminación.
     * @return 204 No Content.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('TRAINER', 'ADMIN')")
    public ResponseEntity<Void> deletePlan(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        workoutPlanService.deletePlan(id, currentUser);
        return ResponseEntity.noContent().build();
    }

    // ===== DIAS =====

    @PostMapping("/{planId}/days")
    @PreAuthorize("hasAnyRole('TRAINER', 'ADMIN')")
    public ResponseEntity<WorkoutDayResponse> addDay(
            @PathVariable Long planId,
            @Valid @RequestBody WorkoutDayRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(workoutPlanService.addDay(planId, request, currentUser));
    }

    @PutMapping("/days/{dayId}")
    @PreAuthorize("hasAnyRole('TRAINER', 'ADMIN')")
    public ResponseEntity<WorkoutDayResponse> updateDay(
            @PathVariable Long dayId,
            @RequestBody WorkoutDayRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(workoutPlanService.updateDay(dayId, request, currentUser));
    }

    @DeleteMapping("/days/{dayId}")
    @PreAuthorize("hasAnyRole('TRAINER', 'ADMIN')")
    public ResponseEntity<Void> deleteDay(
            @PathVariable Long dayId,
            @AuthenticationPrincipal User currentUser) {
        workoutPlanService.deleteDay(dayId, currentUser);
        return ResponseEntity.noContent().build();
    }

    // ===== EJERCICIOS =====

    @PostMapping("/days/{dayId}/exercises")
    @PreAuthorize("hasAnyRole('TRAINER', 'ADMIN')")
    public ResponseEntity<ExerciseResponse> addExercise(
            @PathVariable Long dayId,
            @Valid @RequestBody ExerciseRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(workoutPlanService.addExercise(dayId, request, currentUser));
    }

    @PutMapping("/exercises/{exerciseId}")
    @PreAuthorize("hasAnyRole('TRAINER', 'ADMIN')")
    public ResponseEntity<ExerciseResponse> updateExercise(
            @PathVariable Long exerciseId,
            @RequestBody ExerciseRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(workoutPlanService.updateExercise(exerciseId, request, currentUser));
    }

    @DeleteMapping("/exercises/{exerciseId}")
    @PreAuthorize("hasAnyRole('TRAINER', 'ADMIN')")
    public ResponseEntity<Void> deleteExercise(
            @PathVariable Long exerciseId,
            @AuthenticationPrincipal User currentUser) {
        workoutPlanService.deleteExercise(exerciseId, currentUser);
        return ResponseEntity.noContent().build();
    }
}


