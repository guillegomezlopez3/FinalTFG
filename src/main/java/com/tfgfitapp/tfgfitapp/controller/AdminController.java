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
 * Panel de administracion. Todos los endpoints requieren rol ADMIN.
 *
 * - GET  /api/admin/stats              → estadisticas globales del sistema
 * - GET  /api/admin/trainers           → lista paginada de entrenadores
 * - GET  /api/admin/clients            → lista paginada de clientes
 * - PUT  /api/admin/users/{id}/active  → activa / desactiva un usuario
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
     * GET /api/admin/stats
     * Totales de usuarios, trainers, clientes, dietas activas, etc.
     */
    @GetMapping("/stats")
    public ResponseEntity<AdminStatsResponse> getStats() {
        return ResponseEntity.ok(adminService.getStats());
    }

    /**
     * GET /api/admin/trainers?page=0&size=10&sort=createdAt,desc
     */
    @GetMapping("/trainers")
    public ResponseEntity<PageResponse<TrainerResponse>> getAllTrainers(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(trainerService.getAllTrainers(pageable));
    }

    /**
     * GET /api/admin/clients?page=0&size=10
     */
    @GetMapping("/clients")
    public ResponseEntity<PageResponse<ClientResponse>> getAllClients(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(adminService.getAllClients(pageable));
    }

    /**
     * PUT /api/admin/users/{id}/active
     * Alterna el estado activo/inactivo de un usuario sin eliminarlo.
     */
    @PutMapping("/users/{id}/active")
    public ResponseEntity<Void> toggleUserActive(@PathVariable Long id) {
        adminService.toggleUserActive(id);
        return ResponseEntity.noContent().build();
    }
}

