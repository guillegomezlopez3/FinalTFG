package com.tfgfitapp.tfgfitapp.controller;

import com.tfgfitapp.tfgfitapp.dto.AssignTrainerRequest;
import com.tfgfitapp.tfgfitapp.dto.ClientResponse;
import com.tfgfitapp.tfgfitapp.dto.ClientUpdateRequest;
import com.tfgfitapp.tfgfitapp.dto.CreateClientByTrainerRequest;
import com.tfgfitapp.tfgfitapp.dto.PageResponse;
import com.tfgfitapp.tfgfitapp.entity.User;
import com.tfgfitapp.tfgfitapp.service.ClientService;
import jakarta.validation.Valid;

import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador de gestion de clientes.
 *
 * Endpoints:
 * - POST   /api/clients              → TRAINER: crea y asigna un nuevo cliente a si mismo
 * - GET    /api/clients              → TRAINER: lista paginada de sus clientes
 * - GET    /api/clients/{id}         → TRAINER (propio) / CLIENT (si mismo) / ADMIN
 * - PUT    /api/clients/{id}         → TRAINER (propio) / CLIENT (si mismo) / ADMIN
 * - DELETE /api/clients/{id}         → TRAINER (propio) / ADMIN: desactiva un cliente
 * - PUT    /api/clients/{id}/trainer → ADMIN: asigna entrenador
 */
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    private final ClientService clientService;

    /**
     * POST /api/clients
     * Un TRAINER crea un nuevo cliente y se le asigna automaticamente.
     */
    @PostMapping
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<ClientResponse> createClient(
            @Valid @RequestBody CreateClientByTrainerRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(clientService.createClient(request, currentUser));
    }

    /**
     * GET /api/clients?page=0&size=10&sort=createdAt,desc
     * Un TRAINER obtiene su lista de clientes paginada.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('TRAINER', 'ADMIN')")
    public ResponseEntity<PageResponse<ClientResponse>> getMyClients(
            @AuthenticationPrincipal User currentUser,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(clientService.getMyClients(currentUser, pageable));
    }

    /**
     * GET /api/clients/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('TRAINER', 'CLIENT', 'ADMIN')")
    public ResponseEntity<ClientResponse> getClientById(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(clientService.getClientById(id, currentUser));
    }

    /**
     * PUT /api/clients/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TRAINER', 'CLIENT', 'ADMIN')")
    public ResponseEntity<ClientResponse> updateClient(
            @PathVariable Long id,
            @RequestBody ClientUpdateRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(clientService.updateClient(id, request, currentUser));
    }

    /**
     * DELETE /api/clients/{id}
     * Desactiva un cliente. Solo puede hacerlo su TRAINER o un ADMIN.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('TRAINER', 'ADMIN')")
    public ResponseEntity<Void> deleteClient(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        clientService.deleteClient(id, currentUser);
        return ResponseEntity.noContent().build();
    }

    /**
     * PUT /api/clients/{id}/trainer
     * Asigna un entrenador a un cliente. Solo ADMIN.
     */
    @PutMapping("/{id}/trainer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientResponse> assignTrainer(
            @PathVariable Long id,
            @Valid @RequestBody AssignTrainerRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(clientService.assignTrainer(id, request, currentUser));
    }
}

