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
public class ClientService {

    public ClientService(ClientRepository clientRepository, TrainerRepository trainerRepository,
                         UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.trainerRepository = trainerRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

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

        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(Role.CLIENT);
        newUser.setActive(true);
        
        userRepository.save(newUser);

        Client newClient = new Client();
        newClient.setUser(newUser);
        newClient.setTrainer(trainer);
        newClient.setAge(request.getAge());
        newClient.setGender(request.getGender());
        newClient.setHeight(request.getHeight());
        newClient.setWeight(request.getWeight());
        newClient.setGoal(request.getGoal());
        newClient.setLevel(request.getLevel() != null ? request.getLevel() : com.tfgfitapp.tfgfitapp.enumeration.ClientLevel.BEGINNER);
        newClient.setInjuries(request.getInjuries());
        newClient.setAllergies(request.getAllergies());
        newClient.setNotes(request.getNotes());
        newClient.setActive(true);

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
        ClientResponse response = new ClientResponse();
        response.setId(client.getId());
        response.setUserId(client.getUser() != null ? client.getUser().getId() : null);
        response.setUserName(client.getUser() != null ? client.getUser().getName() : null);
        response.setUserEmail(client.getUser() != null ? client.getUser().getEmail() : null);
        response.setTrainerId(client.getTrainer() != null ? client.getTrainer().getId() : null);
        response.setTrainerName(client.getTrainer() != null && client.getTrainer().getUser() != null
                ? client.getTrainer().getUser().getName() : null);
        response.setAge(client.getAge());
        response.setGender(client.getGender());
        response.setHeight(client.getHeight());
        response.setWeight(client.getWeight());
        response.setGoal(client.getGoal());
        response.setLevel(client.getLevel());
        response.setInjuries(client.getInjuries());
        response.setAllergies(client.getAllergies());
        response.setNotes(client.getNotes());
        response.setActive(client.getActive());
        response.setCreatedAt(client.getCreatedAt());
        return response;
    }
}

