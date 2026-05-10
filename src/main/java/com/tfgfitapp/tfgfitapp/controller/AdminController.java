package com.tfgfitapp.tfgfitapp.controller;

import com.tfgfitapp.tfgfitapp.dto.AdminStatsResponse;
import com.tfgfitapp.tfgfitapp.dto.ClientResponse;
import com.tfgfitapp.tfgfitapp.dto.PageResponse;
import com.tfgfitapp.tfgfitapp.dto.TrainerResponse;
import com.tfgfitapp.tfgfitapp.service.AdminService;
import com.tfgfitapp.tfgfitapp.service.TrainerService;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para la gestión administrativa del sistema.
 * 
 * Este controlador proporciona endpoints protegidos exclusivamente para usuarios con el rol ADMIN.
 * Permite visualizar estadísticas globales, gestionar la lista de entrenadores y clientes,
 * y activar o desactivar usuarios.
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    public AdminController(AdminService adminService, TrainerService trainerService) {
        this.adminService = adminService;
        this.trainerService = trainerService;
    }

    private final AdminService adminService;
    private final TrainerService trainerService;

    /**
     * Obtiene las estadísticas generales del sistema.
     * 
     * @return 200 OK con el desglose de totales (usuarios, entrenadores, clientes, etc.).
     */
    @GetMapping("/stats")
    public ResponseEntity<AdminStatsResponse> getStats() {
        return ResponseEntity.ok(adminService.getStats());
    }

    /**
     * Obtiene una lista paginada de todos los entrenadores registrados.
     * 
     * @param pageable Parámetros de paginación (page, size, sort).
     * @return 200 OK con la página de entrenadores.
     */
    @GetMapping("/trainers")
    public ResponseEntity<PageResponse<TrainerResponse>> getAllTrainers(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(trainerService.getAllTrainers(pageable));
    }

    /**
     * Obtiene una lista paginada de todos los clientes registrados.
     * 
     * @param pageable Parámetros de paginación (page, size, sort).
     * @return 200 OK con la página de clientes.
     */
    @GetMapping("/clients")
    public ResponseEntity<PageResponse<ClientResponse>> getAllClients(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(adminService.getAllClients(pageable));
    }

    /**
     * Alterna el estado de activación de un usuario.
     * 
     * @param id Identificador único del usuario.
     * @return 204 No Content si la operación se realiza con éxito.
     */
    @PutMapping("/users/{id}/active")
    public ResponseEntity<Void> toggleUserActive(@PathVariable Long id) {
        adminService.toggleUserActive(id);
        return ResponseEntity.noContent().build();
    }
}
