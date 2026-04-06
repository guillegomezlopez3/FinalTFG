package com.tfgfitapp.tfgfitapp.service;

import com.tfgfitapp.tfgfitapp.dto.WorkoutPlanRequest;
import com.tfgfitapp.tfgfitapp.dto.WorkoutPlanResponse;
import com.tfgfitapp.tfgfitapp.dto.PageResponse;
import com.tfgfitapp.tfgfitapp.entity.Client;
import com.tfgfitapp.tfgfitapp.entity.Trainer;
import com.tfgfitapp.tfgfitapp.entity.User;
import com.tfgfitapp.tfgfitapp.entity.WorkoutPlan;
import com.tfgfitapp.tfgfitapp.enumeration.Role;
import com.tfgfitapp.tfgfitapp.exception.ResourceNotFoundException;
import com.tfgfitapp.tfgfitapp.repository.ClientRepository;
import com.tfgfitapp.tfgfitapp.repository.ExerciseRepository;
import com.tfgfitapp.tfgfitapp.repository.TrainerRepository;
import com.tfgfitapp.tfgfitapp.repository.WorkoutDayRepository;
import com.tfgfitapp.tfgfitapp.repository.WorkoutPlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios del WorkoutPlanService con Mockito (sin Spring context ni base de datos).
 */
@ExtendWith(MockitoExtension.class)
class WorkoutPlanServiceTest {

    @InjectMocks
    private WorkoutPlanService workoutPlanService;

    @Mock
    private WorkoutPlanRepository workoutPlanRepository;

    @Mock
    private WorkoutDayRepository workoutDayRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private TrainerRepository trainerRepository;

    private User trainerUser;
    private Trainer trainer;
    private User clientUser;
    private Client client;
    private WorkoutPlan workoutPlan;

    @BeforeEach
    void setUp() {
        trainerUser = User.builder()
                .id(1L).name("Carlos Lopez").email("carlos@trainer.com")
                .password("hashed").role(Role.TRAINER).active(true).build();

        trainer = Trainer.builder()
                .id(1L).user(trainerUser).specialty("Fitness").build();

        clientUser = User.builder()
                .id(2L).name("Ana Garcia").email("ana@client.com")
                .password("hashed").role(Role.CLIENT).active(true).build();

        client = Client.builder()
                .id(1L).user(clientUser).trainer(trainer).active(true).build();

        workoutPlan = WorkoutPlan.builder()
                .id(10L)
                .trainer(trainer)
                .client(client)
                .title("Plan Full Body")
                .objective("Hipertrofia")
                .active(true)
                .workoutDays(new ArrayList<>())
                .build();
    }

    // ===== createPlan =====

    @Test
    @DisplayName("createPlan: TRAINER crea un plan para su cliente correctamente")
    void createPlan_validRequest_returnsPlanResponse() {
        WorkoutPlanRequest request = new WorkoutPlanRequest();
        request.setClientId(1L);
        request.setTitle("Plan Full Body");
        request.setObjective("Hipertrofia");

        when(trainerRepository.findByUserId(1L)).thenReturn(Optional.of(trainer));
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(workoutPlanRepository.save(any(WorkoutPlan.class))).thenReturn(workoutPlan);

        WorkoutPlanResponse result = workoutPlanService.createPlan(request, trainerUser);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Plan Full Body");
        assertThat(result.getTrainerId()).isEqualTo(1L);
        assertThat(result.getClientId()).isEqualTo(1L);
        verify(workoutPlanRepository).save(any(WorkoutPlan.class));
    }

    @Test
    @DisplayName("createPlan: TRAINER intenta crear un plan para un cliente ajeno lanza AccessDeniedException")
    void createPlan_clientNotBelongsToTrainer_throwsAccessDeniedException() {
        Trainer otherTrainer = Trainer.builder().id(99L).build();
        Client foreignClient = Client.builder().id(5L).user(clientUser).trainer(otherTrainer).build();

        WorkoutPlanRequest request = new WorkoutPlanRequest();
        request.setClientId(5L);
        request.setTitle("Plan X");

        when(trainerRepository.findByUserId(1L)).thenReturn(Optional.of(trainer));
        when(clientRepository.findById(5L)).thenReturn(Optional.of(foreignClient));

        assertThatThrownBy(() -> workoutPlanService.createPlan(request, trainerUser))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @DisplayName("createPlan: cliente no encontrado lanza ResourceNotFoundException")
    void createPlan_clientNotFound_throwsResourceNotFoundException() {
        WorkoutPlanRequest request = new WorkoutPlanRequest();
        request.setClientId(99L);
        request.setTitle("Plan X");

        when(trainerRepository.findByUserId(1L)).thenReturn(Optional.of(trainer));
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> workoutPlanService.createPlan(request, trainerUser))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ===== getPlansByClient =====

    @Test
    @DisplayName("getPlansByClient: TRAINER obtiene planes de su cliente correctamente")
    void getPlansByClient_trainerOwnsClient_returnsPaginatedPlans() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<WorkoutPlan> page = new PageImpl<>(List.of(workoutPlan), pageable, 1);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(trainerRepository.findByUserId(1L)).thenReturn(Optional.of(trainer));
        when(workoutPlanRepository.findAllByClientIdOrderByCreatedAtDesc(1L, pageable)).thenReturn(page);

        PageResponse<WorkoutPlanResponse> result =
                workoutPlanService.getPlansByClient(1L, null, pageable, trainerUser);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Plan Full Body");
    }

    @Test
    @DisplayName("getPlansByClient: TRAINER intenta ver planes de cliente ajeno lanza AccessDeniedException")
    void getPlansByClient_trainerAccessesForeignClient_throwsAccessDeniedException() {
        Trainer otherTrainer = Trainer.builder().id(99L).build();
        Client foreignClient = Client.builder().id(5L).user(clientUser).trainer(otherTrainer).build();

        when(clientRepository.findById(5L)).thenReturn(Optional.of(foreignClient));
        when(trainerRepository.findByUserId(1L)).thenReturn(Optional.of(trainer));

        assertThatThrownBy(() -> workoutPlanService.getPlansByClient(
                5L, null, PageRequest.of(0, 10), trainerUser))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @DisplayName("getPlansByClient: filtro por active=true usa repositorio especializado")
    void getPlansByClient_withActiveFilter_usesFilteredRepository() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<WorkoutPlan> page = new PageImpl<>(List.of(workoutPlan), pageable, 1);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(trainerRepository.findByUserId(1L)).thenReturn(Optional.of(trainer));
        when(workoutPlanRepository.findAllByClientIdAndActive(1L, true, pageable)).thenReturn(page);

        PageResponse<WorkoutPlanResponse> result =
                workoutPlanService.getPlansByClient(1L, true, pageable, trainerUser);

        assertThat(result.getContent()).hasSize(1);
        verify(workoutPlanRepository, never()).findAllByClientIdOrderByCreatedAtDesc(anyLong(), any(Pageable.class));
        verify(workoutPlanRepository).findAllByClientIdAndActive(1L, true, pageable);
    }

    // ===== getPlanById =====

    @Test
    @DisplayName("getPlanById: TRAINER accede a su propio plan correctamente")
    void getPlanById_trainerAccessesOwnPlan_returnsPlanResponse() {
        when(workoutPlanRepository.findById(10L)).thenReturn(Optional.of(workoutPlan));
        when(trainerRepository.findByUserId(1L)).thenReturn(Optional.of(trainer));

        WorkoutPlanResponse result = workoutPlanService.getPlanById(10L, trainerUser);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(10L);
    }

    @Test
    @DisplayName("getPlanById: plan no encontrado lanza ResourceNotFoundException")
    void getPlanById_notFound_throwsResourceNotFoundException() {
        when(workoutPlanRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> workoutPlanService.getPlanById(99L, trainerUser))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("getPlanById: CLIENT accede a su propio plan correctamente")
    void getPlanById_clientAccessesOwnPlan_returnsPlanResponse() {
        when(workoutPlanRepository.findById(10L)).thenReturn(Optional.of(workoutPlan));

        WorkoutPlanResponse result = workoutPlanService.getPlanById(10L, clientUser);

        assertThat(result).isNotNull();
        assertThat(result.getClientId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("getPlanById: ADMIN accede a cualquier plan correctamente")
    void getPlanById_adminAccessesAnyPlan_returnsPlanResponse() {
        User adminUser = User.builder().id(99L).role(Role.ADMIN).build();
        when(workoutPlanRepository.findById(10L)).thenReturn(Optional.of(workoutPlan));

        WorkoutPlanResponse result = workoutPlanService.getPlanById(10L, adminUser);

        assertThat(result).isNotNull();
    }

    // ===== deletePlan =====

    @Test
    @DisplayName("deletePlan: TRAINER elimina su propio plan correctamente")
    void deletePlan_trainerDeletesOwnPlan_success() {
        when(trainerRepository.findByUserId(1L)).thenReturn(Optional.of(trainer));
        when(workoutPlanRepository.findByIdAndTrainerId(10L, 1L))
                .thenReturn(Optional.of(workoutPlan));

        workoutPlanService.deletePlan(10L, trainerUser);

        verify(workoutPlanRepository).delete(workoutPlan);
    }

    @Test
    @DisplayName("deletePlan: TRAINER intenta eliminar plan ajeno lanza AccessDeniedException")
    void deletePlan_trainerDeletesForeignPlan_throwsAccessDeniedException() {
        when(trainerRepository.findByUserId(1L)).thenReturn(Optional.of(trainer));
        when(workoutPlanRepository.findByIdAndTrainerId(10L, 1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> workoutPlanService.deletePlan(10L, trainerUser))
                .isInstanceOf(AccessDeniedException.class);
    }
}

