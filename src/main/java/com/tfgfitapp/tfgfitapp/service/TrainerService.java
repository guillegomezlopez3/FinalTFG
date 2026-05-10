package com.tfgfitapp.tfgfitapp.service;

import com.tfgfitapp.tfgfitapp.dto.PageResponse;
import com.tfgfitapp.tfgfitapp.dto.TrainerResponse;
import com.tfgfitapp.tfgfitapp.dto.TrainerUpdateRequest;
import com.tfgfitapp.tfgfitapp.entity.Trainer;
import com.tfgfitapp.tfgfitapp.entity.User;
import com.tfgfitapp.tfgfitapp.exception.ResourceNotFoundException;
import com.tfgfitapp.tfgfitapp.repository.ClientRepository;
import com.tfgfitapp.tfgfitapp.repository.TrainerRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para la gestión de perfiles de Entrenadores.
 * 
 * Proporciona lógica para que los entrenadores actualicen su información profesional
 * y para que los administradores supervisen la lista de entrenadores registrados.
 */
@Service
public class TrainerService {

    public TrainerService(TrainerRepository trainerRepository, ClientRepository clientRepository) {
        this.trainerRepository = trainerRepository;
        this.clientRepository = clientRepository;
    }

    private final TrainerRepository trainerRepository;
    private final ClientRepository clientRepository;

    /**
     * Obtiene el perfil del entrenador autenticado.
     * 
     * @param currentUser Usuario autenticado.
     * @return Perfil detallado del entrenador.
     */
    @Transactional(readOnly = true)
    public TrainerResponse getMyProfile(User currentUser) {
        Trainer trainer = findByUserIdOrThrow(currentUser.getId());
        return toResponse(trainer);
    }

    /**
     * Actualiza la información profesional del perfil del entrenador.
     * 
     * @param request Datos de actualización (especialidad, descripción, etc.).
     * @param currentUser Entrenador que realiza la actualización.
     * @return El perfil del entrenador actualizado.
     */
    @Transactional
    public TrainerResponse updateMyProfile(TrainerUpdateRequest request, User currentUser) {
        Trainer trainer = findByUserIdOrThrow(currentUser.getId());

        if (request.getPhone() != null)       trainer.setPhone(request.getPhone());
        if (request.getSpecialty() != null)   trainer.setSpecialty(request.getSpecialty());
        if (request.getDescription() != null) trainer.setDescription(request.getDescription());

        return toResponse(trainerRepository.save(trainer));
    }

    /**
     * ADMIN obtiene el perfil de cualquier entrenador por ID.
     */
    @Transactional(readOnly = true)
    public TrainerResponse getTrainerById(Long trainerId) {
        Trainer trainer = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new ResourceNotFoundException("Entrenador no encontrado con ID: " + trainerId));
        return toResponse(trainer);
    }

    /**
     * ADMIN obtiene la lista paginada de todos los entrenadores.
     * Soporta ?page=0&size=10&sort=createdAt,desc
     */
    @Transactional(readOnly = true)
    public PageResponse<TrainerResponse> getAllTrainers(Pageable pageable) {
        return PageResponse.from(trainerRepository.findAll(pageable).map(this::toResponse));
    }

    // ===== HELPERS =====

    private Trainer findByUserIdOrThrow(Long userId) {
        return trainerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de entrenador no encontrado"));
    }

    public TrainerResponse toResponse(Trainer trainer) {
        long clientCount = clientRepository.countByTrainerId(trainer.getId());
        TrainerResponse response = new TrainerResponse();
        response.setId(trainer.getId());
        response.setUserId(trainer.getUser() != null ? trainer.getUser().getId() : null);
        response.setName(trainer.getUser() != null ? trainer.getUser().getName() : null);
        response.setEmail(trainer.getUser() != null ? trainer.getUser().getEmail() : null);
        response.setPhone(trainer.getPhone());
        response.setSpecialty(trainer.getSpecialty());
        response.setDescription(trainer.getDescription());
        response.setActive(trainer.getUser() != null ? trainer.getUser().getActive() : null);
        response.setCreatedAt(trainer.getCreatedAt());
        response.setClientCount(clientCount);
        return response;
    }
}

