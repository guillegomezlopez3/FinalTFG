package com.tfgfitapp.tfgfitapp.service;

import com.tfgfitapp.tfgfitapp.dto.ClientResponse;
import com.tfgfitapp.tfgfitapp.dto.ClientUpdateRequest;
import com.tfgfitapp.tfgfitapp.entity.Client;
import com.tfgfitapp.tfgfitapp.entity.Trainer;
import com.tfgfitapp.tfgfitapp.entity.User;
import com.tfgfitapp.tfgfitapp.enumeration.ClientLevel;
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
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios del ClientService con Mockito (sin Spring context ni base de datos).
 */
@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private TrainerRepository trainerRepository;

    private User trainerUser;
    private Trainer trainer;
    private User clientUser;
    private Client client;

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
                .id(1L).user(clientUser).trainer(trainer)
                .level(ClientLevel.BEGINNER).active(true).build();
    }

    // ===== getClientById =====

    @Test
    @DisplayName("getClientById: TRAINER accede a su propio cliente - OK")
    void getClientById_trainerAccessesOwnClient_returnsResponse() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(trainerRepository.findByUserId(1L)).thenReturn(Optional.of(trainer));

        ClientResponse result = clientService.getClientById(1L, trainerUser);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUserEmail()).isEqualTo("ana@client.com");
    }

    @Test
    @DisplayName("getClientById: cliente no encontrado lanza ResourceNotFoundException")
    void getClientById_clientNotFound_throwsResourceNotFoundException() {
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clientService.getClientById(99L, trainerUser))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("getClientById: TRAINER intenta acceder a cliente ajeno lanza AccessDeniedException")
    void getClientById_trainerAccessesForeignClient_throwsAccessDeniedException() {
        Trainer otherTrainer = Trainer.builder().id(99L).build();
        Client foreignClient = Client.builder()
                .id(5L).user(clientUser).trainer(otherTrainer).build();

        when(clientRepository.findById(5L)).thenReturn(Optional.of(foreignClient));
        when(trainerRepository.findByUserId(1L)).thenReturn(Optional.of(trainer));

        assertThatThrownBy(() -> clientService.getClientById(5L, trainerUser))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @DisplayName("getClientById: CLIENT accede a su propio perfil - OK")
    void getClientById_clientAccessesOwnProfile_returnsResponse() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        ClientResponse result = clientService.getClientById(1L, clientUser);

        assertThat(result).isNotNull();
        assertThat(result.getUserEmail()).isEqualTo("ana@client.com");
    }

    @Test
    @DisplayName("getClientById: CLIENT intenta ver perfil de otro cliente lanza AccessDeniedException")
    void getClientById_clientAccessesForeignProfile_throwsAccessDeniedException() {
        User otherClientUser = User.builder().id(99L).role(Role.CLIENT).build();
        Client otherClient = Client.builder().id(2L).user(otherClientUser).trainer(trainer).build();

        when(clientRepository.findById(2L)).thenReturn(Optional.of(otherClient));

        assertThatThrownBy(() -> clientService.getClientById(2L, clientUser))
                .isInstanceOf(AccessDeniedException.class);
    }

    // ===== updateClient =====

    @Test
    @DisplayName("updateClient: actualiza campos no nulos correctamente")
    void updateClient_withPartialData_updatesOnlyNonNullFields() {
        ClientUpdateRequest request = new ClientUpdateRequest();
        request.setWeight(java.math.BigDecimal.valueOf(70.5));
        request.setGoal("Definicion muscular");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(trainerRepository.findByUserId(1L)).thenReturn(Optional.of(trainer));
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        ClientResponse result = clientService.updateClient(1L, request, trainerUser);

        assertThat(result).isNotNull();
        verify(clientRepository).save(client);
        assertThat(client.getWeight()).isEqualByComparingTo("70.5");
        assertThat(client.getGoal()).isEqualTo("Definicion muscular");
    }

    // ===== ADMIN =====

    @Test
    @DisplayName("getClientById: ADMIN accede a cualquier cliente - OK")
    void getClientById_adminAccessesAnyClient_returnsResponse() {
        User adminUser = User.builder().id(3L).role(Role.ADMIN).build();
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        ClientResponse result = clientService.getClientById(1L, adminUser);

        assertThat(result).isNotNull();
    }
}

