package com.tfgfitapp.tfgfitapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfgfitapp.tfgfitapp.dto.LoginRequest;
import com.tfgfitapp.tfgfitapp.dto.RegisterRequest;
import com.tfgfitapp.tfgfitapp.dto.TrainerUpdateRequest;
import com.tfgfitapp.tfgfitapp.enumeration.Role;
import com.tfgfitapp.tfgfitapp.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de integración para TrainerController.
 * Usa @SpringBootTest contra MySQL real.
 *
 * Escenarios cubiertos:
 * - GET /api/trainers/me     → TRAINER obtiene su propio perfil
 * - PUT /api/trainers/me     → TRAINER actualiza su perfil
 * - GET /api/trainers/me     → CLIENT recibe 403
 * - GET /api/trainers/me     → Sin token recibe 403
 * - GET /api/trainers        → ADMIN obtiene lista paginada
 * - GET /api/trainers        → TRAINER recibe 403
 * - GET /api/trainers/{id}   → ADMIN obtiene entrenador concreto
 */
@SpringBootTest
@AutoConfigureMockMvc
class TrainerControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;

    // Emails únicos por ejecución para evitar colisiones entre runs
    private static final String TRAINER_EMAIL = "it.trainer." + System.nanoTime() + "@test.com";
    private static final String CLIENT_EMAIL  = "it.client."  + System.nanoTime() + "@test.com";

    private String trainerToken;
    private String clientToken;
    private String adminToken;

    @BeforeEach
    void setUp() throws Exception {
        // 1. Registrar TRAINER
        RegisterRequest trainerReg = RegisterRequest.builder()
                .name("Test Trainer IT").email(TRAINER_EMAIL)
                .password("pass1234").role(Role.TRAINER).build();
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trainerReg)));

        // 2. Registrar CLIENT
        RegisterRequest clientReg = RegisterRequest.builder()
                .name("Test Client IT").email(CLIENT_EMAIL)
                .password("pass1234").role(Role.CLIENT).build();
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientReg)));

        // 3. Login de cada uno y guardar tokens
        trainerToken = loginAndGetToken(TRAINER_EMAIL, "pass1234");
        clientToken  = loginAndGetToken(CLIENT_EMAIL,  "pass1234");
        adminToken   = loginAndGetToken("admin@tfgfitapp.com", "Admin1234!");
    }

    @AfterEach
    void cleanup() {
        userRepository.findByEmail(TRAINER_EMAIL).ifPresent(userRepository::delete);
        userRepository.findByEmail(CLIENT_EMAIL).ifPresent(userRepository::delete);
    }

    // ===== GET /api/trainers/me =====

    @Test
    @DisplayName("GET /trainers/me con token TRAINER devuelve 200 y perfil correcto")
    void getMyProfile_withTrainerToken_returns200() throws Exception {
        mockMvc.perform(get("/api/trainers/me")
                        .header("Authorization", "Bearer " + trainerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(TRAINER_EMAIL))
                .andExpect(jsonPath("$.name").value("Test Trainer IT"))
                .andExpect(jsonPath("$.id").value(notNullValue()));
    }

    @Test
    @DisplayName("GET /trainers/me con token CLIENT devuelve 403")
    void getMyProfile_withClientToken_returns403() throws Exception {
        mockMvc.perform(get("/api/trainers/me")
                        .header("Authorization", "Bearer " + clientToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /trainers/me sin token devuelve 403")
    void getMyProfile_withoutToken_returns403() throws Exception {
        mockMvc.perform(get("/api/trainers/me"))
                .andExpect(status().isForbidden());
    }

    // ===== PUT /api/trainers/me =====

    @Test
    @DisplayName("PUT /trainers/me con token TRAINER actualiza el perfil y devuelve 200")
    void updateMyProfile_withTrainerToken_returns200WithUpdatedData() throws Exception {
        TrainerUpdateRequest request = new TrainerUpdateRequest();
        request.setPhone("666111222");
        request.setSpecialty("Yoga y meditacion");
        request.setDescription("Especialista en yoga y bienestar");

        mockMvc.perform(put("/api/trainers/me")
                        .header("Authorization", "Bearer " + trainerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value("666111222"))
                .andExpect(jsonPath("$.specialty").value("Yoga y meditacion"))
                .andExpect(jsonPath("$.description").value("Especialista en yoga y bienestar"));
    }

    @Test
    @DisplayName("PUT /trainers/me con body vacio no modifica nada y devuelve 200")
    void updateMyProfile_withEmptyBody_returns200WithNoChanges() throws Exception {
        // Primero establecemos un valor conocido
        TrainerUpdateRequest setup = new TrainerUpdateRequest();
        setup.setSpecialty("Pilates");
        mockMvc.perform(put("/api/trainers/me")
                .header("Authorization", "Bearer " + trainerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(setup)));

        // Ahora enviamos body vacío (todos null)
        TrainerUpdateRequest emptyRequest = new TrainerUpdateRequest();
        mockMvc.perform(put("/api/trainers/me")
                        .header("Authorization", "Bearer " + trainerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emptyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.specialty").value("Pilates")); // sigue igual
    }

    @Test
    @DisplayName("PUT /trainers/me con token CLIENT devuelve 403")
    void updateMyProfile_withClientToken_returns403() throws Exception {
        TrainerUpdateRequest request = new TrainerUpdateRequest();
        request.setSpecialty("Crossfit");

        mockMvc.perform(put("/api/trainers/me")
                        .header("Authorization", "Bearer " + clientToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    // ===== GET /api/trainers (ADMIN) =====

    @Test
    @DisplayName("GET /trainers con token ADMIN devuelve 200 y lista paginada")
    void getAllTrainers_withAdminToken_returns200WithPagedList() throws Exception {
        mockMvc.perform(get("/api/trainers")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").isNumber())
                .andExpect(jsonPath("$.page").value(0));
    }

    @Test
    @DisplayName("GET /trainers con token TRAINER devuelve 403")
    void getAllTrainers_withTrainerToken_returns403() throws Exception {
        mockMvc.perform(get("/api/trainers")
                        .header("Authorization", "Bearer " + trainerToken))
                .andExpect(status().isForbidden());
    }

    // ===== GET /api/trainers/{id} (ADMIN) =====

    @Test
    @DisplayName("GET /trainers/{id} con ID valido y token ADMIN devuelve 200")
    void getTrainerById_withAdminToken_returns200() throws Exception {
        // Obtener el ID del trainer recien creado
        String profileJson = mockMvc.perform(get("/api/trainers/me")
                        .header("Authorization", "Bearer " + trainerToken))
                .andReturn().getResponse().getContentAsString();
        Long trainerId = objectMapper.readTree(profileJson).get("id").asLong();

        mockMvc.perform(get("/api/trainers/" + trainerId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(trainerId))
                .andExpect(jsonPath("$.email").value(TRAINER_EMAIL));
    }

    @Test
    @DisplayName("GET /trainers/{id} con ID inexistente devuelve 404")
    void getTrainerById_withNonExistentId_returns404() throws Exception {
        mockMvc.perform(get("/api/trainers/999999")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    // ===== HELPER =====

    private String loginAndGetToken(String email, String password) throws Exception {
        LoginRequest loginReq = LoginRequest.builder()
                .email(email).password(password).build();
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString())
                .get("token").asText();
    }
}

