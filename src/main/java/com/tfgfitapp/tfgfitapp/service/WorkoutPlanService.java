package com.tfgfitapp.tfgfitapp.service;

import com.tfgfitapp.tfgfitapp.dto.*;
import com.tfgfitapp.tfgfitapp.entity.*;
import com.tfgfitapp.tfgfitapp.enumeration.Role;
import com.tfgfitapp.tfgfitapp.exception.ResourceNotFoundException;
import com.tfgfitapp.tfgfitapp.repository.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la planificación y gestión de Entrenamientos.
 * 
 * Permite estructurar planes complejos compuestos por días específicos y ejercicios
 * detallados, facilitando el seguimiento de las rutinas por parte de los clientes.
 */
@Service
public class WorkoutPlanService {

    public WorkoutPlanService(WorkoutPlanRepository workoutPlanRepository, WorkoutDayRepository workoutDayRepository,
                              ExerciseRepository exerciseRepository, ClientRepository clientRepository,
                              TrainerRepository trainerRepository, PredefinedExerciseRepository predefinedExerciseRepository) {
        this.workoutPlanRepository = workoutPlanRepository;
        this.workoutDayRepository = workoutDayRepository;
        this.exerciseRepository = exerciseRepository;
        this.clientRepository = clientRepository;
        this.trainerRepository = trainerRepository;
        this.predefinedExerciseRepository = predefinedExerciseRepository;
    }

    private final WorkoutPlanRepository workoutPlanRepository;
    private final WorkoutDayRepository workoutDayRepository;
    private final ExerciseRepository exerciseRepository;
    private final ClientRepository clientRepository;
    private final TrainerRepository trainerRepository;
    private final PredefinedExerciseRepository predefinedExerciseRepository;

    // ===== PLANES =====

    /**
     * Crea un nuevo plan de entrenamiento estructurado para un cliente.
     * 
     * @param request Datos básicos del plan.
     * @param currentUser Entrenador que prescribe el plan.
     * @return El plan creado con sus metadatos.
     */
    @Transactional
    public WorkoutPlanResponse createPlan(WorkoutPlanRequest request, User currentUser) {
        Trainer trainer = getTrainerOrThrow(currentUser);
        Client client = getClientOrThrow(request.getClientId());
        ensureClientBelongsToTrainer(client, trainer);

        WorkoutPlan plan = new WorkoutPlan();
        plan.setTrainer(trainer);
        plan.setClient(client);
        plan.setTitle(request.getTitle());
        plan.setObjective(request.getObjective());
        plan.setNotes(request.getNotes());
        plan.setStartDate(request.getStartDate());
        plan.setEndDate(request.getEndDate());
        plan.setActive(request.getActive() != null ? request.getActive() : true);

        return toResponse(workoutPlanRepository.save(plan));
    }

    /**
     * Obtiene una página de planes de entrenamiento asociados a un cliente.
     * 
     * @param clientId ID del cliente.
     * @param active Opcional. Filtrar por estado activo/inactivo.
     * @param pageable Parámetros de paginación.
     * @param currentUser Usuario que consulta.
     * @return Página de planes de entrenamiento.
     */
    @Transactional(readOnly = true)
    public PageResponse<WorkoutPlanResponse> getPlansByClient(Long clientId, Boolean active, Pageable pageable, User currentUser) {
        Client client = getClientOrThrow(clientId);
        checkPlanReadAccess(client, currentUser);
        
        Page<WorkoutPlan> planPage;
        if (active != null) {
            planPage = workoutPlanRepository.findAllByClientIdAndActive(clientId, active, pageable);
        } else {
            planPage = workoutPlanRepository.findAllByClientIdOrderByCreatedAtDesc(clientId, pageable);
        }
        
        return PageResponse.from(planPage.map(this::toResponse));
    }

    /**
     * Obtiene los detalles completos de un plan de entrenamiento por su ID.
     * 
     * @param planId ID del plan.
     * @param currentUser Usuario que consulta.
     * @return Respuesta con la información del plan.
     */
    @Transactional(readOnly = true)
    public WorkoutPlanResponse getPlanById(Long planId, User currentUser) {
        WorkoutPlan plan = findPlanOrThrow(planId);
        checkSinglePlanReadAccess(plan, currentUser);
        return toResponse(plan);
    }

    /**
     * TRAINER actualiza un plan propio.
     */
    @Transactional
    public WorkoutPlanResponse updatePlan(Long planId, WorkoutPlanRequest request, User currentUser) {
        Trainer trainer = getTrainerOrThrow(currentUser);
        WorkoutPlan plan = workoutPlanRepository.findByIdAndTrainerId(planId, trainer.getId())
                .orElseThrow(() -> new AccessDeniedException("No tienes acceso a este plan"));

        if (request.getTitle() != null)     plan.setTitle(request.getTitle());
        if (request.getObjective() != null) plan.setObjective(request.getObjective());
        if (request.getNotes() != null)     plan.setNotes(request.getNotes());
        if (request.getStartDate() != null) plan.setStartDate(request.getStartDate());
        if (request.getEndDate() != null)   plan.setEndDate(request.getEndDate());
        if (request.getActive() != null)    plan.setActive(request.getActive());

        return toResponse(workoutPlanRepository.save(plan));
    }

    /**
     * TRAINER elimina un plan propio.
     */
    @Transactional
    public void deletePlan(Long planId, User currentUser) {
        Trainer trainer = getTrainerOrThrow(currentUser);
        WorkoutPlan plan = workoutPlanRepository.findByIdAndTrainerId(planId, trainer.getId())
                .orElseThrow(() -> new AccessDeniedException("No tienes acceso a este plan"));
        workoutPlanRepository.delete(plan);
    }

    // ===== DÍAS DE ENTRENAMIENTO (WorkoutDay) =====

    /**
     * Añade un día de entrenamiento a un plan existente.
     * 
     * @param planId ID del plan.
     * @param request Datos del día (ej. Lunes - Pecho).
     * @param currentUser Entrenador que añade el día.
     * @return El día de entrenamiento creado.
     */
    @Transactional
    public WorkoutDayResponse addDay(Long planId, WorkoutDayRequest request, User currentUser) {
        Trainer trainer = getTrainerOrThrow(currentUser);
        WorkoutPlan plan = workoutPlanRepository.findByIdAndTrainerId(planId, trainer.getId())
                .orElseThrow(() -> new AccessDeniedException("No tienes acceso a este plan"));

        WorkoutDay day = new WorkoutDay();
        day.setWorkoutPlan(plan);
        day.setDayOfWeek(request.getDayOfWeek());
        day.setFocus(request.getFocus());
        day.setNotes(request.getNotes());

        return toDayResponse(workoutDayRepository.save(day));
    }

    /**
     * TRAINER actualiza un día de un plan propio.
     */
    @Transactional
    public WorkoutDayResponse updateDay(Long dayId, WorkoutDayRequest request, User currentUser) {
        Trainer trainer = getTrainerOrThrow(currentUser);
        WorkoutDay day = getDayOrThrow(dayId);

        if (!day.getWorkoutPlan().getTrainer().getId().equals(trainer.getId())) {
            throw new AccessDeniedException("No tienes acceso a este día");
        }

        if (request.getDayOfWeek() != null) day.setDayOfWeek(request.getDayOfWeek());
        if (request.getFocus() != null)     day.setFocus(request.getFocus());
        if (request.getNotes() != null)     day.setNotes(request.getNotes());

        return toDayResponse(workoutDayRepository.save(day));
    }

    /**
     * TRAINER elimina un día de un plan propio.
     */
    @Transactional
    public void deleteDay(Long dayId, User currentUser) {
        Trainer trainer = getTrainerOrThrow(currentUser);
        WorkoutDay day = getDayOrThrow(dayId);

        if (!day.getWorkoutPlan().getTrainer().getId().equals(trainer.getId())) {
            throw new AccessDeniedException("No tienes acceso a este día");
        }
        workoutDayRepository.delete(day);
    }

    // ===== EJERCICIOS =====

    /**
     * Añade un ejercicio específico a un día de entrenamiento.
     * 
     * @param dayId ID del día de entrenamiento.
     * @param request Datos del ejercicio (series, reps, etc.).
     * @param currentUser Entrenador que añade el ejercicio.
     * @return El ejercicio creado.
     */
    @Transactional
    public ExerciseResponse addExercise(Long dayId, ExerciseRequest request, User currentUser) {
        Trainer trainer = getTrainerOrThrow(currentUser);
        WorkoutDay day = getDayOrThrow(dayId);

        if (!day.getWorkoutPlan().getTrainer().getId().equals(trainer.getId())) {
            throw new AccessDeniedException("No tienes acceso a este día");
        }

        Exercise exercise = new Exercise();
        exercise.setWorkoutDay(day);
        exercise.setName(request.getName());
        exercise.setSets(request.getSets());
        exercise.setReps(request.getReps());
        exercise.setRestSeconds(request.getRestSeconds());
        exercise.setDurationMinutes(request.getDurationMinutes());
        exercise.setNotes(request.getNotes());
        exercise.setGifUrl(request.getGifUrl());
        if (request.getPredefinedExerciseId() != null) {
            predefinedExerciseRepository.findById(request.getPredefinedExerciseId())
                    .ifPresent(exercise::setPredefinedExercise);
        }

        return toExerciseResponse(exerciseRepository.save(exercise));
    }

    /**
     * TRAINER actualiza un ejercicio propio.
     */
    @Transactional
    public ExerciseResponse updateExercise(Long exerciseId, ExerciseRequest request, User currentUser) {
        Trainer trainer = getTrainerOrThrow(currentUser);
        Exercise exercise = getExerciseOrThrow(exerciseId);

        if (!exercise.getWorkoutDay().getWorkoutPlan().getTrainer().getId().equals(trainer.getId())) {
            throw new AccessDeniedException("No tienes acceso a este ejercicio");
        }

        if (request.getName() != null)            exercise.setName(request.getName());
        if (request.getSets() != null)            exercise.setSets(request.getSets());
        if (request.getReps() != null)            exercise.setReps(request.getReps());
        if (request.getRestSeconds() != null)     exercise.setRestSeconds(request.getRestSeconds());
        if (request.getDurationMinutes() != null) exercise.setDurationMinutes(request.getDurationMinutes());
        if (request.getNotes() != null)           exercise.setNotes(request.getNotes());
        if (request.getGifUrl() != null)          exercise.setGifUrl(request.getGifUrl());
        if (request.getPredefinedExerciseId() != null) {
            predefinedExerciseRepository.findById(request.getPredefinedExerciseId())
                    .ifPresent(exercise::setPredefinedExercise);
        }

        return toExerciseResponse(exerciseRepository.save(exercise));
    }

    /**
     * TRAINER elimina un ejercicio propio.
     */
    @Transactional
    public void deleteExercise(Long exerciseId, User currentUser) {
        Trainer trainer = getTrainerOrThrow(currentUser);
        Exercise exercise = getExerciseOrThrow(exerciseId);

        if (!exercise.getWorkoutDay().getWorkoutPlan().getTrainer().getId().equals(trainer.getId())) {
            throw new AccessDeniedException("No tienes acceso a este ejercicio");
        }
        exerciseRepository.delete(exercise);
    }

    // ===== HELPERS DE ACCESO =====

    private void checkPlanReadAccess(Client client, User currentUser) {
        if (currentUser.getRole() == Role.ADMIN) return;
        if (currentUser.getRole() == Role.TRAINER) {
            Trainer trainer = getTrainerOrThrow(currentUser);
            if (client.getTrainer() == null || !client.getTrainer().getId().equals(trainer.getId())) {
                throw new AccessDeniedException("Este cliente no es tuyo");
            }
        } else if (currentUser.getRole() == Role.CLIENT) {
            if (!client.getUser().getId().equals(currentUser.getId())) {
                throw new AccessDeniedException("Solo puedes ver tus propios planes");
            }
        }
    }

    private void checkSinglePlanReadAccess(WorkoutPlan plan, User currentUser) {
        if (currentUser.getRole() == Role.ADMIN) return;
        if (currentUser.getRole() == Role.TRAINER) {
            Trainer trainer = getTrainerOrThrow(currentUser);
            if (!plan.getTrainer().getId().equals(trainer.getId())) {
                throw new AccessDeniedException("No tienes acceso a este plan");
            }
        } else if (currentUser.getRole() == Role.CLIENT) {
            if (!plan.getClient().getUser().getId().equals(currentUser.getId())) {
                throw new AccessDeniedException("Solo puedes ver tus propios planes");
            }
        }
    }

    private void ensureClientBelongsToTrainer(Client client, Trainer trainer) {
        if (client.getTrainer() == null || !client.getTrainer().getId().equals(trainer.getId())) {
            throw new AccessDeniedException("Este cliente no está asignado a ti");
        }
    }

    // ===== LOOKUPS =====

    private WorkoutPlan findPlanOrThrow(Long id) {
        return workoutPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plan no encontrado con ID: " + id));
    }

    private WorkoutDay getDayOrThrow(Long id) {
        return workoutDayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Día de entrenamiento no encontrado con ID: " + id));
    }

    private Exercise getExerciseOrThrow(Long id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ejercicio no encontrado con ID: " + id));
    }

    private Client getClientOrThrow(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
    }

    private Trainer getTrainerOrThrow(User user) {
        return trainerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de entrenador no encontrado"));
    }

    // ===== MAPPERS =====

    public WorkoutPlanResponse toResponse(WorkoutPlan plan) {
        List<WorkoutDayResponse> daysList = plan.getWorkoutDays() != null
                ? plan.getWorkoutDays().stream().map(this::toDayResponse).collect(Collectors.toList())
                : List.of();

        WorkoutPlanResponse response = new WorkoutPlanResponse();
        response.setId(plan.getId());
        response.setClientId(plan.getClient().getId());
        response.setClientName(plan.getClient().getUser() != null ? plan.getClient().getUser().getName() : null);
        response.setTrainerId(plan.getTrainer().getId());
        response.setTrainerName(plan.getTrainer().getUser() != null ? plan.getTrainer().getUser().getName() : null);
        response.setTitle(plan.getTitle());
        response.setObjective(plan.getObjective());
        response.setNotes(plan.getNotes());
        response.setStartDate(plan.getStartDate());
        response.setEndDate(plan.getEndDate());
        response.setActive(plan.getActive());
        response.setCreatedAt(plan.getCreatedAt());
        response.setUpdatedAt(plan.getUpdatedAt());
        response.setWorkoutDays(daysList);
        return response;
    }

    public WorkoutDayResponse toDayResponse(WorkoutDay day) {
        List<ExerciseResponse> exercisesList = day.getExercises() != null
                ? day.getExercises().stream().map(this::toExerciseResponse).collect(Collectors.toList())
                : List.of();

        WorkoutDayResponse response = new WorkoutDayResponse();
        response.setId(day.getId());
        response.setWorkoutPlanId(day.getWorkoutPlan() != null ? day.getWorkoutPlan().getId() : null);
        response.setDayOfWeek(day.getDayOfWeek());
        response.setFocus(day.getFocus());
        response.setNotes(day.getNotes());
        response.setExercises(exercisesList);
        return response;
    }

    public ExerciseResponse toExerciseResponse(Exercise exercise) {
        ExerciseResponse response = new ExerciseResponse();
        response.setId(exercise.getId());
        response.setWorkoutDayId(exercise.getWorkoutDay() != null ? exercise.getWorkoutDay().getId() : null);
        response.setName(exercise.getName());
        response.setSets(exercise.getSets());
        response.setReps(exercise.getReps());
        response.setRestSeconds(exercise.getRestSeconds());
        response.setDurationMinutes(exercise.getDurationMinutes());
        response.setNotes(exercise.getNotes());
        response.setGifUrl(exercise.getGifUrl());
        if (exercise.getPredefinedExercise() != null) {
            response.setPredefinedExerciseId(exercise.getPredefinedExercise().getId());
        }
        return response;
    }
}

