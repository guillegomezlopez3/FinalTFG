package com.tfgfitapp.tfgfitapp.service;

import com.tfgfitapp.tfgfitapp.dto.ExerciseProgressRequest;
import com.tfgfitapp.tfgfitapp.dto.ExerciseProgressResponse;
import com.tfgfitapp.tfgfitapp.entity.*;
import com.tfgfitapp.tfgfitapp.exception.ResourceNotFoundException;
import com.tfgfitapp.tfgfitapp.repository.ClientRepository;
import com.tfgfitapp.tfgfitapp.repository.ExerciseProgressRepository;
import com.tfgfitapp.tfgfitapp.repository.PredefinedExerciseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para el seguimiento del progreso en ejercicios específicos.
 * 
 * Permite registrar las cargas (peso y repeticiones) para ejercicios del catálogo
 * predefinido, permitiendo visualizar la evolución histórica del cliente.
 */
@Service
public class ExerciseProgressService {

    private final ExerciseProgressRepository repository;
    private final ClientRepository clientRepository;
    private final PredefinedExerciseRepository predefinedExerciseRepository;

    public ExerciseProgressService(ExerciseProgressRepository repository, 
                                   ClientRepository clientRepository, 
                                   PredefinedExerciseRepository predefinedExerciseRepository) {
        this.repository = repository;
        this.clientRepository = clientRepository;
        this.predefinedExerciseRepository = predefinedExerciseRepository;
    }

    /**
     * Registra una nueva marca de progreso para un ejercicio concreto.
     * 
     * @param request Datos de la carga levantada y el ejercicio asociado.
     * @param currentUser Cliente que realiza el registro.
     * @return Respuesta con los datos del registro guardado.
     */
    @Transactional
    public ExerciseProgressResponse createProgress(ExerciseProgressRequest request, User currentUser) {
        Client client = clientRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de cliente no encontrado"));

        PredefinedExercise predefinedExercise = null;
        if (request.getPredefinedExerciseId() != null) {
            predefinedExercise = predefinedExerciseRepository.findById(request.getPredefinedExerciseId())
                    .orElse(null);
        }

        ExerciseProgress progress = new ExerciseProgress(
                client, 
                predefinedExercise, 
                request.getExerciseName(), 
                request.getWeight(), 
                request.getReps(), 
                request.getDate()
        );
        return toResponse(repository.save(progress));
    }

    @Transactional(readOnly = true)
    public List<ExerciseProgressResponse> getProgressByExercise(Long predefinedExerciseId, String exerciseName, User currentUser) {
        Client client = clientRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de cliente no encontrado"));

        if (predefinedExerciseId != null) {
            return repository.findAllByClientIdAndPredefinedExerciseIdOrderByDateDesc(client.getId(), predefinedExerciseId)
                    .stream().map(this::toResponse).collect(Collectors.toList());
        } else {
            return repository.findAllByClientIdAndExerciseNameOrderByDateDesc(client.getId(), exerciseName)
                    .stream().map(this::toResponse).collect(Collectors.toList());
        }
    }

    private ExerciseProgressResponse toResponse(ExerciseProgress progress) {
        ExerciseProgressResponse response = new ExerciseProgressResponse();
        response.setId(progress.getId());
        response.setClientId(progress.getClient().getId());
        if (progress.getPredefinedExercise() != null) {
            response.setPredefinedExerciseId(progress.getPredefinedExercise().getId());
        }
        response.setExerciseName(progress.getExerciseName());
        response.setWeight(progress.getWeight());
        response.setReps(progress.getReps());
        response.setDate(progress.getDate());
        response.setCreatedAt(progress.getCreatedAt());
        return response;
    }
}
