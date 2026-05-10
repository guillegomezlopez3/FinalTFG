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
 * Controlador para la gestión de Registros de Progreso físico.
 * 
 * Permite a los clientes consultar sus propias medidas corporales,
 * y a los entrenadores registrar y visualizar la evolución de sus clientes asignados.
 */
@RestController
@RequestMapping("/api/progress")
public class ProgressRecordController {

    public ProgressRecordController(ProgressRecordService progressRecordService) {
        this.progressRecordService = progressRecordService;
    }

    private final ProgressRecordService progressRecordService;

    /**
     * Crea un nuevo registro de progreso.
     * 
     * @param request Datos de las medidas físicas.
     * @param currentUser Usuario autenticado.
     * @return 201 Created con el registro de progreso guardado.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENT', 'TRAINER', 'ADMIN')")
    public ResponseEntity<ProgressRecordResponse> createRecord(
            @Valid @RequestBody ProgressRecordRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(progressRecordService.createRecord(request, currentUser));
    }

    /**
     * Obtiene todos los registros de progreso del cliente autenticado.
     * 
     * @param currentUser Cliente autenticado.
     * @return Lista de registros ordenados por fecha.
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<ProgressRecordResponse>> getMyRecords(
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(progressRecordService.getMyRecords(currentUser));
    }

    /**
     * Obtiene los registros de progreso de un cliente específico.
     * 
     * @param clientId Identificador del cliente.
     * @param currentUser Usuario que realiza la consulta (entrenador o administrador).
     * @return Lista de registros del cliente.
     */
    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasAnyRole('TRAINER', 'ADMIN')")
    public ResponseEntity<List<ProgressRecordResponse>> getRecordsByClient(
            @PathVariable Long clientId,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(progressRecordService.getRecordsByClient(clientId, currentUser));
    }

    /**
     * Obtiene un registro de progreso específico por su ID.
     * 
     * @param id Identificador del registro.
     * @param currentUser Usuario que realiza la consulta.
     * @return El registro encontrado.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENT', 'TRAINER', 'ADMIN')")
    public ResponseEntity<ProgressRecordResponse> getRecordById(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(progressRecordService.getRecordById(id, currentUser));
    }

    /**
     * Elimina un registro de progreso.
     * 
     * @param id Identificador del registro.
     * @param currentUser Usuario que realiza la eliminación.
     * @return 204 No Content.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN', 'TRAINER')")
    public ResponseEntity<Void> deleteRecord(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        progressRecordService.deleteRecord(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}
