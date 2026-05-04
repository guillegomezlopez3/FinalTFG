package com.tfgfitapp.tfgfitapp.controller;

import com.tfgfitapp.tfgfitapp.dto.ProgressRecordRequest;
import com.tfgfitapp.tfgfitapp.dto.ProgressRecordResponse;
import com.tfgfitapp.tfgfitapp.entity.User;
import com.tfgfitapp.tfgfitapp.service.ProgressRecordService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador de registros de progreso físico.
 *
 * Endpoints:
 * - POST   /api/progress              → CLIENT: crea su propio registro
 * - GET    /api/progress/me           → CLIENT: obtiene sus propios registros
 * - GET    /api/progress/client/{id}  → TRAINER (propio) / ADMIN
 * - GET    /api/progress/{id}         → CLIENT (suyo) / TRAINER (propio) / ADMIN
 * - DELETE /api/progress/{id}         → CLIENT (suyo) / ADMIN
 */
@RestController
@RequestMapping("/api/progress")
public class ProgressRecordController {

    public ProgressRecordController(ProgressRecordService progressRecordService) {
        this.progressRecordService = progressRecordService;
    }

    private final ProgressRecordService progressRecordService;

    /**
     * POST /api/progress
     * El cliente autenticado registra sus propias medidas del día.
     */
    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ProgressRecordResponse> createRecord(
            @Valid @RequestBody ProgressRecordRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(progressRecordService.createRecord(request, currentUser));
    }

    /**
     * GET /api/progress/me
     * El cliente obtiene su propio historial de progreso (más reciente primero).
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<ProgressRecordResponse>> getMyRecords(
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(progressRecordService.getMyRecords(currentUser));
    }

    /**
     * GET /api/progress/client/{clientId}
     * El entrenador o admin consulta el historial de un cliente específico.
     */
    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasAnyRole('TRAINER', 'ADMIN')")
    public ResponseEntity<List<ProgressRecordResponse>> getRecordsByClient(
            @PathVariable Long clientId,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(progressRecordService.getRecordsByClient(clientId, currentUser));
    }

    /**
     * GET /api/progress/{id}
     * Obtiene un registro concreto con control de acceso.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENT', 'TRAINER', 'ADMIN')")
    public ResponseEntity<ProgressRecordResponse> getRecordById(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(progressRecordService.getRecordById(id, currentUser));
    }

    /**
     * DELETE /api/progress/{id}
     * El cliente elimina su propio registro. El admin puede eliminar cualquiera.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<Void> deleteRecord(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        progressRecordService.deleteRecord(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}

