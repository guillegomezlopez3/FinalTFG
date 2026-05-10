package com.tfgfitapp.tfgfitapp.controller;

import com.tfgfitapp.tfgfitapp.dto.ExerciseProgressRequest;
import com.tfgfitapp.tfgfitapp.dto.ExerciseProgressResponse;
import com.tfgfitapp.tfgfitapp.entity.User;
import com.tfgfitapp.tfgfitapp.service.ExerciseProgressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para el registro del progreso en ejercicios específicos.
 * 
 * Permite a los clientes registrar el peso y repeticiones logrados en sus entrenamientos
 * para mantener un historial evolutivo por cada ejercicio.
 */
@RestController
@RequestMapping("/api/exercise-progress")
public class ExerciseProgressController {

    private final ExerciseProgressService service;

    public ExerciseProgressController(ExerciseProgressService service) {
        this.service = service;
    }

    /**
     * Registra un nuevo hito de progreso (peso/reps) para un ejercicio.
     * 
     * @param request Datos del progreso.
     * @param currentUser Cliente autenticado.
     * @return 201 Created con el registro de progreso guardado.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENT', 'TRAINER')")
    public ResponseEntity<ExerciseProgressResponse> logProgress(
            @Valid @RequestBody ExerciseProgressRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createProgress(request, currentUser));
    }

    /**
     * Obtiene el historial de progreso de un ejercicio específico.
     */
    @GetMapping("/exercise")
    @PreAuthorize("hasAnyRole('CLIENT', 'TRAINER')")
    public ResponseEntity<List<ExerciseProgressResponse>> getProgressByExercise(
            @RequestParam(required = false) Long predefinedExerciseId,
            @RequestParam(required = false) String name,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(service.getProgressByExercise(predefinedExerciseId, name, currentUser));
    }
}
