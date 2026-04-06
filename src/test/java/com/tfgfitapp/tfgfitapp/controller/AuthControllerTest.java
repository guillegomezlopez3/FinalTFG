package com.tfgfitapp.tfgfitapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfgfitapp.tfgfitapp.dto.LoginRequest;
import com.tfgfitapp.tfgfitapp.dto.RegisterRequest;
import com.tfgfitapp.tfgfitapp.enumeration.Role;
import com.tfgfitapp.tfgfitapp.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de integracion del controlador de autenticacion.
 * Usa @SpringBootTest contra MySQL real + limpieza en @AfterEach.
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;

    // Email unico por ejecucion de test para evitar colisiones
    private static final String TRAINER_EMAIL = "test.trainer." + System.nanoTime() + "@test.com";
    private static final String CLIENT_EMAIL  = "test.client."  + System.nanoTime() + "@test.com";

    @AfterEach
    void cleanup() {
        // Borra el User; CascadeType.ALL en User->Trainer/Client se encarga del perfil
        userRepository.findByEmail(TRAINER_EMAIL).ifPresent(userRepository::delete);
        userRepository.findByEmail(CLIENT_EMAIL).ifPresent(userRepository::delete);
    }

    // ===== REGISTER =====

    @Test
    @DisplayName("POST /register con datos validos de TRAINER devuelve 201 y token")
    void register_validTrainer_returns201WithToken() throws Exception {
        RegisterRequest req = RegisterRequest.builder()
                .name("Test Trainer")
                .email(TRAINER_EMAIL)
                .password("password123")
                .role(Role.TRAINER)
                .build();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.email").value(TRAINER_EMAIL))
                .andExpect(jsonPath("$.role").value("TRAINER"));
    }

    @Test
    @DisplayName("POST /register con datos validos de CLIENT devuelve 201 y token")
    void register_validClient_returns201WithToken() throws Exception {
        RegisterRequest req = RegisterRequest.builder()
                .name("Test Client")
                .email(CLIENT_EMAIL)
                .password("password123")
                .role(Role.CLIENT)
                .build();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.role").value("CLIENT"));
    }

    @Test
    @DisplayName("POST /register sin nombre devuelve 400")
    void register_withoutName_returns400() throws Exception {
        RegisterRequest req = RegisterRequest.builder()
                .email("noname@test.com")
                .password("password123")
                .role(Role.TRAINER)
                .build();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /register con email invalido devuelve 400")
    void register_withInvalidEmail_returns400() throws Exception {
        RegisterRequest req = RegisterRequest.builder()
                .name("Test")
                .email("not-an-email")
                .password("password123")
                .role(Role.TRAINER)
                .build();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    // ===== LOGIN =====

    @Test
    @DisplayName("POST /login: registrar y luego hacer login devuelve 200 y token")
    void login_afterRegister_returns200WithToken() throws Exception {
        // 1. Registrar
        RegisterRequest reg = RegisterRequest.builder()
                .name("Test Trainer")
                .email(TRAINER_EMAIL)
                .password("password123")
                .role(Role.TRAINER)
                .build();
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reg)));

        // 2. Login
        LoginRequest login = LoginRequest.builder()
                .email(TRAINER_EMAIL)
                .password("password123")
                .build();

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.role").value("TRAINER"));
    }

    @Test
    @DisplayName("POST /login con password incorrecta devuelve 401")
    void login_withWrongPassword_returns401() throws Exception {
        // Registrar primero
        RegisterRequest reg = RegisterRequest.builder()
                .name("Test Trainer")
                .email(TRAINER_EMAIL)
                .password("password123")
                .role(Role.TRAINER)
                .build();
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reg)));

        // Login con password incorrecta
        LoginRequest login = LoginRequest.builder()
                .email(TRAINER_EMAIL)
                .password("wrong-password")
                .build();

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /login sin email devuelve 400")
    void login_withoutEmail_returns400() throws Exception {
        LoginRequest login = LoginRequest.builder()
                .password("password123")
                .build();

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isBadRequest());
    }
}
