package com.tfgfitapp.tfgfitapp.controller;

import com.tfgfitapp.tfgfitapp.dto.*;
import com.tfgfitapp.tfgfitapp.entity.User;
import com.tfgfitapp.tfgfitapp.service.WorkoutPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador de gestion de planes de entrenamiento, dias y ejercicios.
 */
@RestController
@RequestMapping("/api/workout-plans")
@RequiredArgsConstructor
public class WorkoutPlanController {

    private final WorkoutPlanService workoutPlanService;

    // ===== PLANES =====

    @PostMapping
    @PreAuthorize("hasAnyRole('TRAINER', 'ADMIN')")
    public ResponseEntity<WorkoutPlanResponse> createPlan(
            @Valid @RequestBody WorkoutPlanRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(workoutPlanService.createPlan(request, currentUser));
    }

    /**
     * GET /api/workout-plans/client/{clientId}?active=true&page=0&size=10
     * Parametro active es opcional.
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


