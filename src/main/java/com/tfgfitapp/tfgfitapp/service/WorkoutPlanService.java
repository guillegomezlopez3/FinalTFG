package com.tfgfitapp.tfgfitapp.service;

import com.tfgfitapp.tfgfitapp.dto.*;
import com.tfgfitapp.tfgfitapp.entity.*;
import com.tfgfitapp.tfgfitapp.enumeration.Role;
import com.tfgfitapp.tfgfitapp.exception.ResourceNotFoundException;
import com.tfgfitapp.tfgfitapp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de gestión de planes de entrenamiento, días y ejercicios.
 *
 * Control de acceso:
 * - TRAINER: crea, edita y elimina planes para sus propios clientes.
 * - CLIENT: solo puede leer sus propios planes.
 * - ADMIN: acceso completo.
 */
@Service
@RequiredArgsConstructor
public class WorkoutPlanService {

    private final WorkoutPlanRepository workoutPlanRepository;
    private final WorkoutDayRepository workoutDayRepository;
    private final ExerciseRepository exerciseRepository;
    private final ClientRepository clientRepository;
    private final TrainerRepository trainerRepository;

    // ===== PLANES =====

    /**
     * TRAINER crea un plan de entrenamiento para uno de sus clientes.
     */
    @Transactional
    public WorkoutPlanResponse createPlan(WorkoutPlanRequest request, User currentUser) {
        Trainer trainer = getTrainerOrThrow(currentUser);
        Client client = getClientOrThrow(request.getClientId());
        ensureClientBelongsToTrainer(client, trainer);

        WorkoutPlan plan = WorkoutPlan.builder()
                .trainer(trainer)
                .client(client)
                .title(request.getTitle())
                .objective(request.getObjective())
                .notes(request.getNotes())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .active(request.getActive() != null ? request.getActive() : true)
                .build();

        return toResponse(workoutPlanRepository.save(plan));
    }

    /**
     * Obtiene todos los planes de un cliente con control de acceso, paginados y filtrados opcionalmente por active.
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
     * Obtiene un plan por ID con control de acceso.
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
     * TRAINER añade un día a un plan propio.
     */
    @Transactional
    public WorkoutDayResponse addDay(Long planId, WorkoutDayRequest request, User currentUser) {
        Trainer trainer = getTrainerOrThrow(currentUser);
        WorkoutPlan plan = workoutPlanRepository.findByIdAndTrainerId(planId, trainer.getId())
                .orElseThrow(() -> new AccessDeniedException("No tienes acceso a este plan"));

        WorkoutDay day = WorkoutDay.builder()
                .workoutPlan(plan)
                .dayOfWeek(request.getDayOfWeek())
                .focus(request.getFocus())
                .notes(request.getNotes())
                .build();

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
     * TRAINER añade un ejercicio a un día propio.
     */
    @Transactional
    public ExerciseResponse addExercise(Long dayId, ExerciseRequest request, User currentUser) {
        Trainer trainer = getTrainerOrThrow(currentUser);
        WorkoutDay day = getDayOrThrow(dayId);

        if (!day.getWorkoutPlan().getTrainer().getId().equals(trainer.getId())) {
            throw new AccessDeniedException("No tienes acceso a este día");
        }

        Exercise exercise = Exercise.builder()
                .workoutDay(day)
                .name(request.getName())
                .sets(request.getSets())
                .reps(request.getReps())
                .restSeconds(request.getRestSeconds())
                .durationMinutes(request.getDurationMinutes())
                .notes(request.getNotes())
                .build();

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
        List<WorkoutDayResponse> days = plan.getWorkoutDays() != null
                ? plan.getWorkoutDays().stream().map(this::toDayResponse).collect(Collectors.toList())
                : List.of();

        return WorkoutPlanResponse.builder()
                .id(plan.getId())
                .clientId(plan.getClient().getId())
                .clientName(plan.getClient().getUser() != null ? plan.getClient().getUser().getName() : null)
                .trainerId(plan.getTrainer().getId())
                .trainerName(plan.getTrainer().getUser() != null ? plan.getTrainer().getUser().getName() : null)
                .title(plan.getTitle())
                .objective(plan.getObjective())
                .notes(plan.getNotes())
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .active(plan.getActive())
                .createdAt(plan.getCreatedAt())
                .updatedAt(plan.getUpdatedAt())
                .workoutDays(days)
                .build();
    }

    public WorkoutDayResponse toDayResponse(WorkoutDay day) {
        List<ExerciseResponse> exercises = day.getExercises() != null
                ? day.getExercises().stream().map(this::toExerciseResponse).collect(Collectors.toList())
                : List.of();

        return WorkoutDayResponse.builder()
                .id(day.getId())
                .workoutPlanId(day.getWorkoutPlan() != null ? day.getWorkoutPlan().getId() : null)
                .dayOfWeek(day.getDayOfWeek())
                .focus(day.getFocus())
                .notes(day.getNotes())
                .exercises(exercises)
                .build();
    }

    public ExerciseResponse toExerciseResponse(Exercise exercise) {
        return ExerciseResponse.builder()
                .id(exercise.getId())
                .workoutDayId(exercise.getWorkoutDay() != null ? exercise.getWorkoutDay().getId() : null)
                .name(exercise.getName())
                .sets(exercise.getSets())
                .reps(exercise.getReps())
                .restSeconds(exercise.getRestSeconds())
                .durationMinutes(exercise.getDurationMinutes())
                .notes(exercise.getNotes())
                .build();
    }
}

