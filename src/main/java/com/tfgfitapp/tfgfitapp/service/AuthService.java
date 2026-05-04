package com.tfgfitapp.tfgfitapp.service;

import com.tfgfitapp.tfgfitapp.dto.AuthResponse;
import com.tfgfitapp.tfgfitapp.dto.LoginRequest;
import com.tfgfitapp.tfgfitapp.dto.RegisterRequest;
import com.tfgfitapp.tfgfitapp.entity.Client;
import com.tfgfitapp.tfgfitapp.entity.Trainer;
import com.tfgfitapp.tfgfitapp.entity.User;
import com.tfgfitapp.tfgfitapp.enumeration.Role;
import com.tfgfitapp.tfgfitapp.repository.ClientRepository;
import com.tfgfitapp.tfgfitapp.repository.TrainerRepository;
import com.tfgfitapp.tfgfitapp.repository.UserRepository;
import com.tfgfitapp.tfgfitapp.security.JwtService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de autenticación. Gestiona el registro y login de usuarios.
 *
 * Decisión de diseño:
 * - El registro de ADMIN no está disponible por endpoint público por seguridad.
 *   Se puede crear manualmente en la BBDD o via un endpoint protegido en el futuro.
 * - Al registrar un TRAINER se crea automáticamente su entidad Trainer asociada.
 * - Al registrar un CLIENT se crea su entidad Client con trainer=null (se asignará después).
 */
@Service
public class AuthService {

    public AuthService(UserRepository userRepository, TrainerRepository trainerRepository,
                       ClientRepository clientRepository, PasswordEncoder passwordEncoder,
                       JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.trainerRepository = trainerRepository;
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Registra un nuevo usuario TRAINER o CLIENT.
     * Lanza IllegalArgumentException si el email ya existe o si se intenta registrar un ADMIN.
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Validar que no sea intento de registro de ADMIN por endpoint público
        if (request.getRole() == Role.ADMIN) {
            throw new IllegalArgumentException("No está permitido registrar usuarios ADMIN por este endpoint.");
        }

        // Validar email único
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con el email: " + request.getEmail());
        }

        // Crear y guardar el User base
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setActive(true);

        userRepository.save(user);

        // Crear entidad de perfil según el rol
        if (request.getRole() == Role.TRAINER) {
            Trainer trainer = new Trainer();
            trainer.setUser(user);
            trainerRepository.save(trainer);
        } else if (request.getRole() == Role.CLIENT) {
            Client client = new Client();
            client.setUser(user);
            client.setTrainer(null); // Se asignará después por el admin o trainer
            clientRepository.save(client);
        }

        String token = jwtService.generateToken(user);

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setUserId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        return response;
    }

    /**
     * Autentica un usuario existente y devuelve un JWT.
     * Spring Security lanza AuthenticationException si las credenciales son incorrectas.
     */
    public AuthResponse login(LoginRequest request) {
        // authenticationManager valida email + password, lanza excepción si falla
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        String token = jwtService.generateToken(user);

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setUserId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        return response;
    }
}

