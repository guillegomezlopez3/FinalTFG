package com.tfgfitapp.tfgfitapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfgfitapp.tfgfitapp.dto.LoginRequest;
import com.tfgfitapp.tfgfitapp.dto.RegisterRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de integración para AdminController.
 * Usa @SpringBootTest contra MySQL real.
 *
 * Escenarios cubiertos:
 * - GET /api/admin/stats             → ADMIN obtiene estadísticas globales
 * - GET /api/admin/stats             → TRAINER recibe 403
 * - GET /api/admin/stats             → Sin token recibe 403
 * - GET /api/admin/trainers          → ADMIN obtiene lista paginada de trainers
 * - GET /api/admin/trainers?page=... → Parámetros de paginación respetados
 * - GET /api/admin/clients           → ADMIN obtiene lista paginada de clientes
 * - PUT /api/admin/users/{id}/active → ADMIN alterna estado activo/inactivo
 * - PUT /api/admin/users/{id}/active → TRAINER recibe 403
 */
@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;

    private static final String TRAINER_EMAIL = "admin.it.trainer." + System.nanoTime() + "@test.com";
    private static final String CLIENT_EMAIL  = "admin.it.client."  + System.nanoTime() + "@test.com";

    private String adminToken;
    private String trainerToken;
    private Long trainerUserId;

    @BeforeEach
    void setUp() throws Exception {
        // Registrar TRAINER y CLIENT de prueba
        RegisterRequest trainerReg = RegisterRequest.builder()
                .name("Admin IT Trainer").email(TRAINER_EMAIL)
                .password("pass1234").role(Role.TRAINER).build();
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trainerReg)));

        RegisterRequest clientReg = RegisterRequest.builder()
                .name("Admin IT Client").email(CLIENT_EMAIL)
                .password("pass1234").role(Role.CLIENT).build();
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientReg)));

        adminToken   = loginAndGetToken("admin@tfgfitapp.com", "Admin1234!");
        trainerToken = loginAndGetToken(TRAINER_EMAIL, "pass1234");

        // Guardar el ID del User del trainer para el test de toggle
        trainerUserId = userRepository.findByEmail(TRAINER_EMAIL)
                .orElseThrow().getId();
    }

    @AfterEach
    void cleanup() {
        // Asegurar que el usuario queda activo aunque el test lo desactivara
        userRepository.findByEmail(TRAINER_EMAIL).ifPresent(u -> {
            u.setActive(true);
            userRepository.save(u);
        });
        userRepository.findByEmail(TRAINER_EMAIL).ifPresent(userRepository::delete);
        userRepository.findByEmail(CLIENT_EMAIL).ifPresent(userRepository::delete);
    }

    // ===== GET /api/admin/stats =====

    @Test
    @DisplayName("GET /admin/stats con token ADMIN devuelve 200 con todos los campos")
    void getStats_withAdminToken_returns200WithAllFields() throws Exception {
        mockMvc.perform(get("/api/admin/stats")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalUsers").isNumber())
                .andExpect(jsonPath("$.totalTrainers").isNumber())
                .andExpect(jsonPath("$.totalClients").isNumber())
                .andExpect(jsonPath("$.activeClients").isNumber())
                .andExpect(jsonPath("$.inactiveClients").isNumber())
                .andExpect(jsonPath("$.totalDiets").isNumber())
                .andExpect(jsonPath("$.activeDiets").isNumber())
                .andExpect(jsonPath("$.totalWorkoutPlans").isNumber())
                .andExpect(jsonPath("$.activeWorkoutPlans").isNumber())
                .andExpect(jsonPath("$.totalProgressRecords").isNumber());
    }

    @Test
    @DisplayName("GET /admin/stats con token TRAINER devuelve 403")
    void getStats_withTrainerToken_returns403() throws Exception {
        mockMvc.perform(get("/api/admin/stats")
                        .header("Authorization", "Bearer " + trainerToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /admin/stats sin token devuelve 403")
    void getStats_withoutToken_returns403() throws Exception {
        mockMvc.perform(get("/api/admin/stats"))
                .andExpect(status().isForbidden());
    }

    // ===== GET /api/admin/trainers =====

    @Test
    @DisplayName("GET /admin/trainers con token ADMIN devuelve 200 con estructura paginada")
    void getAllTrainers_withAdminToken_returns200WithPaginatedStructure() throws Exception {
        mockMvc.perform(get("/api/admin/trainers")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").isNumber())
                .andExpect(jsonPath("$.totalElements").isNumber())
                .andExpect(jsonPath("$.totalPages").isNumber())
                .andExpect(jsonPath("$.first").value(true));
    }

    @Test
    @DisplayName("GET /admin/trainers con size=1 devuelve solo un elemento por pagina")
    void getAllTrainers_withSizeParam_respectsPagination() throws Exception {
        mockMvc.perform(get("/api/admin/trainers?size=1&page=0")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(1))
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    @DisplayName("GET /admin/trainers el trainer creado en setUp aparece en los resultados")
    void getAllTrainers_newTrainerAppearsInList() throws Exception {
        mockMvc.perform(get("/api/admin/trainers?size=100")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[?(@.email == '" + TRAINER_EMAIL + "')]").exists());
    }

    @Test
    @DisplayName("GET /admin/trainers con token TRAINER devuelve 403")
    void getAllTrainers_withTrainerToken_returns403() throws Exception {
        mockMvc.perform(get("/api/admin/trainers")
                        .header("Authorization", "Bearer " + trainerToken))
                .andExpect(status().isForbidden());
    }

    // ===== GET /api/admin/clients =====

    @Test
    @DisplayName("GET /admin/clients con token ADMIN devuelve 200 con estructura paginada")
    void getAllClients_withAdminToken_returns200WithPaginatedStructure() throws Exception {
        mockMvc.perform(get("/api/admin/clients")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").isNumber());
    }

    @Test
    @DisplayName("GET /admin/clients el cliente creado en setUp aparece en los resultados")
    void getAllClients_newClientAppearsInList() throws Exception {
        mockMvc.perform(get("/api/admin/clients?size=100")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[?(@.userEmail == '" + CLIENT_EMAIL + "')]").exists());
    }

    @Test
    @DisplayName("GET /admin/clients con token TRAINER devuelve 403")
    void getAllClients_withTrainerToken_returns403() throws Exception {
        mockMvc.perform(get("/api/admin/clients")
                        .header("Authorization", "Bearer " + trainerToken))
                .andExpect(status().isForbidden());
    }

    // ===== PUT /api/admin/users/{id}/active =====

    @Test
    @DisplayName("PUT /admin/users/{id}/active alterna el estado activo y devuelve 204")
    void toggleUserActive_withAdminToken_returns204AndTogglesState() throws Exception {
        // Primera llamada: desactiva
        mockMvc.perform(put("/api/admin/users/" + trainerUserId + "/active")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());

        boolean activoTrasDesactivar = userRepository.findById(trainerUserId)
                .orElseThrow().getActive();
        org.assertj.core.api.Assertions.assertThat(activoTrasDesactivar).isFalse();

        // Segunda llamada: reactiva
        mockMvc.perform(put("/api/admin/users/" + trainerUserId + "/active")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());

        boolean activoTrasReactivar = userRepository.findById(trainerUserId)
                .orElseThrow().getActive();
        org.assertj.core.api.Assertions.assertThat(activoTrasReactivar).isTrue();
    }

    @Test
    @DisplayName("PUT /admin/users/999999/active con ID inexistente devuelve 404")
    void toggleUserActive_withNonExistentId_returns404() throws Exception {
        mockMvc.perform(put("/api/admin/users/999999/active")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /admin/users/{id}/active con token TRAINER devuelve 403")
    void toggleUserActive_withTrainerToken_returns403() throws Exception {
        mockMvc.perform(put("/api/admin/users/" + trainerUserId + "/active")
                        .header("Authorization", "Bearer " + trainerToken))
                .andExpect(status().isForbidden());
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

