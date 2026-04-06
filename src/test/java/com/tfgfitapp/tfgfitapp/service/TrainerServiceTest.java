package com.tfgfitapp.tfgfitapp.service;

import com.tfgfitapp.tfgfitapp.dto.TrainerResponse;
import com.tfgfitapp.tfgfitapp.dto.TrainerUpdateRequest;
import com.tfgfitapp.tfgfitapp.entity.Trainer;
import com.tfgfitapp.tfgfitapp.entity.User;
import com.tfgfitapp.tfgfitapp.enumeration.Role;
import com.tfgfitapp.tfgfitapp.exception.ResourceNotFoundException;
import com.tfgfitapp.tfgfitapp.repository.ClientRepository;
import com.tfgfitapp.tfgfitapp.repository.TrainerRepository;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios del TrainerService con Mockito (sin Spring context ni base de datos).
 */
@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @InjectMocks
    private TrainerService trainerService;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private ClientRepository clientRepository;

    private User trainerUser;
    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainerUser = User.builder()
                .id(1L).name("Carlos Lopez").email("carlos@trainer.com")
                .password("hashed").role(Role.TRAINER).active(true).build();

        trainer = Trainer.builder()
                .id(1L).user(trainerUser)
                .phone("600000001")
                .specialty("Fitness")
                .description("Entrenador personal certificado")
                .build();
    }

    // ===== getMyProfile =====

    @Test
    @DisplayName("getMyProfile: devuelve el perfil del entrenador autenticado")
    void getMyProfile_returnsTrainerResponse() {
        when(trainerRepository.findByUserId(1L)).thenReturn(Optional.of(trainer));
        when(clientRepository.countByTrainerId(1L)).thenReturn(3L);

        TrainerResponse result = trainerService.getMyProfile(trainerUser);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Carlos Lopez");
        assertThat(result.getEmail()).isEqualTo("carlos@trainer.com");
        assertThat(result.getSpecialty()).isEqualTo("Fitness");
        assertThat(result.getClientCount()).isEqualTo(3L);
    }

    @Test
    @DisplayName("getMyProfile: usuario sin perfil de entrenador lanza ResourceNotFoundException")
    void getMyProfile_noTrainerProfile_throwsResourceNotFoundException() {
        when(trainerRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trainerService.getMyProfile(trainerUser))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // ===== updateMyProfile =====

    @Test
    @DisplayName("updateMyProfile: actualiza solo los campos no nulos del request")
    void updateMyProfile_updatesOnlyNonNullFields() {
        TrainerUpdateRequest request = new TrainerUpdateRequest();
        request.setPhone("699999999");
        request.setSpecialty("Crossfit");
        // description == null → no debe modificarse

        when(trainerRepository.findByUserId(1L)).thenReturn(Optional.of(trainer));
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);
        when(clientRepository.countByTrainerId(1L)).thenReturn(2L);

        TrainerResponse result = trainerService.updateMyProfile(request, trainerUser);

        assertThat(trainer.getPhone()).isEqualTo("699999999");
        assertThat(trainer.getSpecialty()).isEqualTo("Crossfit");
        assertThat(trainer.getDescription()).isEqualTo("Entrenador personal certificado"); // sin cambio
        assertThat(result).isNotNull();
        verify(trainerRepository).save(trainer);
    }

    @Test
    @DisplayName("updateMyProfile: request con todos los campos nulos no modifica nada")
    void updateMyProfile_allNullRequest_noFieldsChanged() {
        TrainerUpdateRequest request = new TrainerUpdateRequest();
        // todos null

        when(trainerRepository.findByUserId(1L)).thenReturn(Optional.of(trainer));
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);
        when(clientRepository.countByTrainerId(1L)).thenReturn(0L);

        trainerService.updateMyProfile(request, trainerUser);

        assertThat(trainer.getPhone()).isEqualTo("600000001");
        assertThat(trainer.getSpecialty()).isEqualTo("Fitness");
        assertThat(trainer.getDescription()).isEqualTo("Entrenador personal certificado");
    }

    // ===== getTrainerById =====

    @Test
    @DisplayName("getTrainerById: ADMIN obtiene un entrenador concreto por ID")
    void getTrainerById_existingId_returnsTrainerResponse() {
        when(trainerRepository.findById(1L)).thenReturn(Optional.of(trainer));
        when(clientRepository.countByTrainerId(1L)).thenReturn(5L);

        TrainerResponse result = trainerService.getTrainerById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getClientCount()).isEqualTo(5L);
    }

    @Test
    @DisplayName("getTrainerById: ID inexistente lanza ResourceNotFoundException")
    void getTrainerById_notFound_throwsResourceNotFoundException() {
        when(trainerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trainerService.getTrainerById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ===== getAllTrainers =====

    @Test
    @DisplayName("getAllTrainers: devuelve lista paginada con el contenido correcto")
    void getAllTrainers_returnsPaginatedList() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Trainer> page = new PageImpl<>(List.of(trainer), pageable, 1);

        when(trainerRepository.findAll(pageable)).thenReturn(page);
        when(clientRepository.countByTrainerId(1L)).thenReturn(2L);

        var result = trainerService.getAllTrainers(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Carlos Lopez");
    }
}

