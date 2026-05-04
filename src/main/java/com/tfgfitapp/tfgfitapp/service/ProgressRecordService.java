package com.tfgfitapp.tfgfitapp.service;

import com.tfgfitapp.tfgfitapp.dto.ProgressRecordRequest;
import com.tfgfitapp.tfgfitapp.dto.ProgressRecordResponse;
import com.tfgfitapp.tfgfitapp.entity.Client;
import com.tfgfitapp.tfgfitapp.entity.ProgressRecord;
import com.tfgfitapp.tfgfitapp.entity.Trainer;
import com.tfgfitapp.tfgfitapp.entity.User;
import com.tfgfitapp.tfgfitapp.enumeration.Role;
import com.tfgfitapp.tfgfitapp.exception.ResourceNotFoundException;
import com.tfgfitapp.tfgfitapp.repository.ClientRepository;
import com.tfgfitapp.tfgfitapp.repository.ProgressRecordRepository;
import com.tfgfitapp.tfgfitapp.repository.TrainerRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de gestión de registros de progreso.
 *
 * Control de acceso:
 * - CLIENT: crea y consulta sus propios registros.
 * - TRAINER: consulta registros de sus clientes.
 * - ADMIN: acceso completo.
 */
@Service
public class ProgressRecordService {

    public ProgressRecordService(ProgressRecordRepository progressRecordRepository,
                                 ClientRepository clientRepository, TrainerRepository trainerRepository) {
        this.progressRecordRepository = progressRecordRepository;
        this.clientRepository = clientRepository;
        this.trainerRepository = trainerRepository;
    }

    private final ProgressRecordRepository progressRecordRepository;
    private final ClientRepository clientRepository;
    private final TrainerRepository trainerRepository;

    /**
     * CLIENT crea un registro de progreso para sí mismo.
     */
    @Transactional
    public ProgressRecordResponse createRecord(ProgressRecordRequest request, User currentUser) {
        Client client = clientRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de cliente no encontrado"));

        ProgressRecord record = new ProgressRecord();
        record.setClient(client);
        record.setRecordDate(request.getRecordDate());
        record.setWeight(request.getWeight());
        record.setBodyFat(request.getBodyFat());
        record.setChest(request.getChest());
        record.setWaist(request.getWaist());
        record.setHips(request.getHips());
        record.setArms(request.getArms());
        record.setLegs(request.getLegs());
        record.setNotes(request.getNotes());

        return toResponse(progressRecordRepository.save(record));
    }

    /**
     * Obtiene todos los registros de un cliente con control de acceso.
     */
    @Transactional(readOnly = true)
    public List<ProgressRecordResponse> getRecordsByClient(Long clientId, User currentUser) {
        Client client = getClientOrThrow(clientId);
        checkReadAccess(client, currentUser);
        return progressRecordRepository.findAllByClientIdOrderByRecordDateDesc(clientId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * CLIENT obtiene sus propios registros de progreso (sin necesitar conocer su clientId).
     */
    @Transactional(readOnly = true)
    public List<ProgressRecordResponse> getMyRecords(User currentUser) {
        Client client = clientRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de cliente no encontrado"));
        return progressRecordRepository.findAllByClientIdOrderByRecordDateDesc(client.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un registro por ID con control de acceso.
     */
    @Transactional(readOnly = true)
    public ProgressRecordResponse getRecordById(Long recordId, User currentUser) {
        ProgressRecord record = getRecordOrThrow(recordId);
        checkSingleReadAccess(record, currentUser);
        return toResponse(record);
    }

    /**
     * CLIENT elimina su propio registro de progreso.
     */
    @Transactional
    public void deleteRecord(Long recordId, User currentUser) {
        ProgressRecord record = getRecordOrThrow(recordId);
        if (currentUser.getRole() != Role.ADMIN) {
            if (!record.getClient().getUser().getId().equals(currentUser.getId())) {
                throw new AccessDeniedException("Solo puedes eliminar tus propios registros");
            }
        }
        progressRecordRepository.delete(record);
    }

    // ===== HELPERS DE ACCESO =====

    private void checkReadAccess(Client client, User currentUser) {
        if (currentUser.getRole() == Role.ADMIN) return;
        if (currentUser.getRole() == Role.TRAINER) {
            Trainer trainer = trainerRepository.findByUserId(currentUser.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Perfil de entrenador no encontrado"));
            if (client.getTrainer() == null || !client.getTrainer().getId().equals(trainer.getId())) {
                throw new AccessDeniedException("Este cliente no es tuyo");
            }
        } else if (currentUser.getRole() == Role.CLIENT) {
            if (!client.getUser().getId().equals(currentUser.getId())) {
                throw new AccessDeniedException("Solo puedes ver tus propios registros");
            }
        }
    }

    private void checkSingleReadAccess(ProgressRecord record, User currentUser) {
        if (currentUser.getRole() == Role.ADMIN) return;
        if (currentUser.getRole() == Role.TRAINER) {
            Trainer trainer = trainerRepository.findByUserId(currentUser.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Perfil de entrenador no encontrado"));
            if (record.getClient().getTrainer() == null
                    || !record.getClient().getTrainer().getId().equals(trainer.getId())) {
                throw new AccessDeniedException("No tienes acceso a este registro");
            }
        } else if (currentUser.getRole() == Role.CLIENT) {
            if (!record.getClient().getUser().getId().equals(currentUser.getId())) {
                throw new AccessDeniedException("Solo puedes ver tus propios registros");
            }
        }
    }

    // ===== LOOKUPS =====

    private Client getClientOrThrow(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
    }

    private ProgressRecord getRecordOrThrow(Long id) {
        return progressRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de progreso no encontrado con ID: " + id));
    }

    // ===== MAPPER =====

    public ProgressRecordResponse toResponse(ProgressRecord record) {
        ProgressRecordResponse response = new ProgressRecordResponse();
        response.setId(record.getId());
        response.setClientId(record.getClient().getId());
        response.setClientName(record.getClient().getUser() != null ? record.getClient().getUser().getName() : null);
        response.setRecordDate(record.getRecordDate());
        response.setWeight(record.getWeight());
        response.setBodyFat(record.getBodyFat());
        response.setChest(record.getChest());
        response.setWaist(record.getWaist());
        response.setHips(record.getHips());
        response.setArms(record.getArms());
        response.setLegs(record.getLegs());
        response.setNotes(record.getNotes());
        response.setCreatedAt(record.getCreatedAt());
        return response;
    }
}

