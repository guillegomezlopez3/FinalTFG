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
 * Controlador para la gestión de Clientes.
 * 
 * Proporciona endpoints para que los entrenadores creen y gestionen sus clientes,
 * para que los clientes consulten su propio perfil, y para que los administradores
 * asignen entrenadores a los clientes.
 */
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    private final ClientService clientService;

    /**
     * Crea un nuevo cliente y lo vincula al entrenador actual.
     * 
     * @param request Datos del nuevo cliente a crear.
     * @param currentUser Entrenador que realiza la creación.
     * @return 200 OK con los datos del cliente recién creado.
     */
    @PostMapping
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<ClientResponse> createClient(
            @Valid @RequestBody CreateClientByTrainerRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(clientService.createClient(request, currentUser));
    }

    /**
     * Obtiene la lista paginada de clientes asociados al usuario autenticado.
     * 
     * @param currentUser Entrenador o administrador autenticado.
     * @param pageable Parámetros de paginación.
     * @return 200 OK con la página de clientes.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('TRAINER', 'ADMIN')")
    public ResponseEntity<PageResponse<ClientResponse>> getMyClients(
            @AuthenticationPrincipal User currentUser,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(clientService.getMyClients(currentUser, pageable));
    }

    /**
     * Obtiene los detalles de un cliente específico por su ID.
     * 
     * @param id Identificador único del cliente.
     * @param currentUser Usuario que realiza la consulta (debe tener permisos sobre el cliente).
     * @return 200 OK con el perfil detallado del cliente.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('TRAINER', 'CLIENT', 'ADMIN')")
    public ResponseEntity<ClientResponse> getClientById(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(clientService.getClientById(id, currentUser));
    }

    /**
     * Actualiza la información de un cliente.
     * 
     * @param id Identificador del cliente.
     * @param request Datos actualizados.
     * @param currentUser Usuario que realiza la actualización.
     * @return 200 OK con los datos del cliente actualizados.
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
     * Desactiva el perfil de un cliente (borrado lógico).
     * 
     * @param id Identificador del cliente.
     * @param currentUser Usuario que realiza la baja.
     * @return 204 No Content si se desactiva correctamente.
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
     * Asigna un entrenador a un cliente específico.
     * 
     * @param id Identificador del cliente.
     * @param request Datos de la asignación (ID del entrenador).
     * @param currentUser Administrador autenticado.
     * @return 200 OK con el cliente actualizado.
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

