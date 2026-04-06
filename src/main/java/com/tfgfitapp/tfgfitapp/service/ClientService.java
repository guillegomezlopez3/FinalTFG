package com.tfgfitapp.tfgfitapp.service;

import com.tfgfitapp.tfgfitapp.dto.AssignTrainerRequest;
import com.tfgfitapp.tfgfitapp.dto.ClientResponse;
import com.tfgfitapp.tfgfitapp.dto.ClientUpdateRequest;
import com.tfgfitapp.tfgfitapp.dto.PageResponse;
import com.tfgfitapp.tfgfitapp.entity.Client;
import com.tfgfitapp.tfgfitapp.entity.Trainer;
import com.tfgfitapp.tfgfitapp.entity.User;
import com.tfgfitapp.tfgfitapp.enumeration.Role;
import com.tfgfitapp.tfgfitapp.exception.ResourceNotFoundException;
import com.tfgfitapp.tfgfitapp.repository.ClientRepository;
import com.tfgfitapp.tfgfitapp.repository.TrainerRepository;
import com.tfgfitapp.tfgfitapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de gestión de clientes.
 *
 * Control de acceso:
 * - ADMIN: puede ver y gestionar cualquier cliente.
 * - TRAINER: solo puede ver/editar sus propios clientes.
 * - CLIENT: solo puede ver/editar su propio perfil.
 */
@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ===== LECTURA =====

    /**
     * El TRAINER obtiene la lista paginada de sus propios clientes.
     */
    @Transactional(readOnly = true)
    public PageResponse<ClientResponse> getMyClients(User currentUser, Pageable pageable) {
        Trainer trainer = getTrainerOrThrow(currentUser);
        Page<Client> clientPage = clientRepository.findAllByTrainerIdOrderByCreatedAtDesc(trainer.getId(), pageable);
        return PageResponse.from(clientPage.map(this::toResponse));
    }

    /**
     * Obtiene un cliente por ID con control de acceso:
     * - TRAINER: debe ser su cliente.
     * - CLIENT: debe ser él mismo.
     * - ADMIN: sin restricción.
     */
    @Transactional(readOnly = true)
    public ClientResponse getClientById(Long clientId, User currentUser) {
        Client client = findClientOrThrow(clientId);
        checkReadAccess(client, currentUser);
        return toResponse(client);
    }

    // ===== ACTUALIZACIN =====

    /**
     * El TRAINER crea y asigna un nuevo cliente a s mismo.
     */
    @Transactional
    public ClientResponse createClient(com.tfgfitapp.tfgfitapp.dto.CreateClientByTrainerRequest request, User currentUser) {
        Trainer trainer = getTrainerOrThrow(currentUser);

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con el correo: " + request.getEmail());
        }

        User newUser = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CLIENT)
                .active(true)
                .build();
        
        userRepository.save(newUser);

        Client newClient = Client.builder()
                .user(newUser)
                .trainer(trainer)
                .age(request.getAge())
                .gender(request.getGender())
                .height(request.getHeight())
                .weight(request.getWeight())
                .goal(request.getGoal())
                .level(request.getLevel() != null ? request.getLevel() : com.tfgfitapp.tfgfitapp.enumeration.ClientLevel.BEGINNER)
                .injuries(request.getInjuries())
                .allergies(request.getAllergies())
                .notes(request.getNotes())
                .active(true)
                .build();

        return toResponse(clientRepository.save(newClient));
    }

    /**
     * Actualiza los datos de un cliente. Solo campos no nulos del request.
     * - TRAINER puede editar cualquiera de sus clientes.
     * - CLIENT puede editar su propio perfil.
     * - ADMIN puede editar cualquier cliente.
     */
    @Transactional
    public ClientResponse updateClient(Long clientId, ClientUpdateRequest request, User currentUser) {
        Client client = findClientOrThrow(clientId);
        checkWriteAccess(client, currentUser);

        if (request.getAge() != null)        client.setAge(request.getAge());
        if (request.getGender() != null)     client.setGender(request.getGender());
        if (request.getHeight() != null)     client.setHeight(request.getHeight());
        if (request.getWeight() != null)     client.setWeight(request.getWeight());
        if (request.getGoal() != null)       client.setGoal(request.getGoal());
        if (request.getLevel() != null)      client.setLevel(request.getLevel());
        if (request.getInjuries() != null)   client.setInjuries(request.getInjuries());
        if (request.getAllergies() != null)   client.setAllergies(request.getAllergies());
        if (request.getNotes() != null)      client.setNotes(request.getNotes());
        if (request.getActive() != null)     client.setActive(request.getActive());

        return toResponse(clientRepository.save(client));
    }

    /**
     * Asigna o reasigna un entrenador a un cliente. Solo ADMIN.
     */
    @Transactional
    public ClientResponse assignTrainer(Long clientId, AssignTrainerRequest request, User currentUser) {
        if (currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Solo el ADMIN puede asignar entrenadores");
        }
        Client client = findClientOrThrow(clientId);
        Trainer trainer = trainerRepository.findById(request.getTrainerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Entrenador no encontrado con ID: " + request.getTrainerId()));
        client.setTrainer(trainer);
        return toResponse(clientRepository.save(client));
    }

    /**
     * Desactiva (Soft-delete) un cliente.
     */
    @Transactional
    public void deleteClient(Long clientId, User currentUser) {
        Client client = findClientOrThrow(clientId);
        checkWriteAccess(client, currentUser);
        
        client.setActive(false);
        if (client.getUser() != null) {
            client.getUser().setActive(false);
            userRepository.save(client.getUser());
        }
        clientRepository.save(client);
    }

    // ===== HELPERS DE ACCESO =====

    private void checkReadAccess(Client client, User currentUser) {
        Role role = currentUser.getRole();
        if (role == Role.ADMIN) return;

        if (role == Role.TRAINER) {
            Trainer trainer = getTrainerOrThrow(currentUser);
            if (client.getTrainer() == null || !client.getTrainer().getId().equals(trainer.getId())) {
                throw new AccessDeniedException("No puedes acceder a este cliente");
            }
        } else if (role == Role.CLIENT) {
            if (!client.getUser().getId().equals(currentUser.getId())) {
                throw new AccessDeniedException("Solo puedes ver tu propio perfil");
            }
        }
    }

    private void checkWriteAccess(Client client, User currentUser) {
        Role role = currentUser.getRole();
        if (role == Role.ADMIN) return;

        if (role == Role.TRAINER) {
            Trainer trainer = getTrainerOrThrow(currentUser);
            if (client.getTrainer() == null || !client.getTrainer().getId().equals(trainer.getId())) {
                throw new AccessDeniedException("No puedes editar a este cliente");
            }
        } else if (role == Role.CLIENT) {
            if (!client.getUser().getId().equals(currentUser.getId())) {
                throw new AccessDeniedException("Solo puedes editar tu propio perfil");
            }
        }
    }

    private Client findClientOrThrow(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + clientId));
    }

    private Trainer getTrainerOrThrow(User user) {
        return trainerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de entrenador no encontrado"));
    }

    // ===== MAPPER =====

    public ClientResponse toResponse(Client client) {
        return ClientResponse.builder()
                .id(client.getId())
                .userId(client.getUser() != null ? client.getUser().getId() : null)
                .userName(client.getUser() != null ? client.getUser().getName() : null)
                .userEmail(client.getUser() != null ? client.getUser().getEmail() : null)
                .trainerId(client.getTrainer() != null ? client.getTrainer().getId() : null)
                .trainerName(client.getTrainer() != null && client.getTrainer().getUser() != null
                        ? client.getTrainer().getUser().getName() : null)
                .age(client.getAge())
                .gender(client.getGender())
                .height(client.getHeight())
                .weight(client.getWeight())
                .goal(client.getGoal())
                .level(client.getLevel())
                .injuries(client.getInjuries())
                .allergies(client.getAllergies())
                .notes(client.getNotes())
                .active(client.getActive())
                .createdAt(client.getCreatedAt())
                .build();
    }
}

