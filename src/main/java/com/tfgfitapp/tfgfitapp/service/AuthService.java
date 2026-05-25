package com.tfgfitapp.tfgfitapp.service;

import com.tfgfitapp.tfgfitapp.dto.AuthResponse;
import com.tfgfitapp.tfgfitapp.dto.LoginRequest;
import com.tfgfitapp.tfgfitapp.dto.RegisterRequest;
import com.tfgfitapp.tfgfitapp.entity.Trainer;
import com.tfgfitapp.tfgfitapp.entity.User;
import com.tfgfitapp.tfgfitapp.enumeration.Role;
import com.tfgfitapp.tfgfitapp.repository.TrainerRepository;
import com.tfgfitapp.tfgfitapp.repository.UserRepository;
import com.tfgfitapp.tfgfitapp.security.JwtService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio central para la autenticación y registro de usuarios.
 * 
 * Gestiona el ciclo de vida de la autenticación mediante JWT, el registro de entrenadores
 * con integración de pagos en Stripe y el inicio de sesión seguro.
 */
/**
 * Servicio para la gestión del restablecimiento de contraseñas.
 * 
 * Genera tokens únicos de un solo uso y tiempo limitado para permitir a los usuarios
 * recuperar el acceso a sus cuentas mediante el cambio de contraseña.
 */
@Service
public class AuthService {

    public AuthService(UserRepository userRepository, TrainerRepository trainerRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService, AuthenticationManager authenticationManager,
                       StripeService stripeService, EmailService emailService) {
        this.userRepository = userRepository;
        this.trainerRepository = trainerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.stripeService = stripeService;
        this.emailService = emailService;
    }

    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final StripeService stripeService;
    private final EmailService emailService;

    /**
     * Registra un nuevo usuario TRAINER en el sistema.
     * 
     * Realiza las siguientes acciones:
     * 1. Valida que el email no esté registrado.
     * 2. Crea el usuario base con rol TRAINER.
     * 3. Crea la entidad Trainer asociada.
     * 4. Genera el token JWT.
     * 5. Inicia una sesión de checkout en Stripe.
     * 
     * @param request Datos del registro.
     * @return AuthResponse con el token y URL de pago.
     * @throws IllegalArgumentException Si el email ya existe.
     */
    /**
     * Crea un token de restablecimiento para el email proporcionado.
     * 
     * @param email Correo electrónico del usuario.
     * @return El token generado (UUID).
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Validar email único
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con el email: " + request.getEmail());
        }

        // Crear y guardar el User base
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.TRAINER); // Siempre es TRAINER por registro público
        user.setActive(false); // No activo hasta confirmar
        user.setEmailConfirmed(false); // Entrenadores deben confirmar por email

        userRepository.save(user);

        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainerRepository.save(trainer);

        String token = jwtService.generateToken(user);

        // Crear sesión de Stripe (suscripción anual con 10 días de trial)
        String checkoutUrl = stripeService.createTrainerCheckoutSession(user, trainer);

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setUserId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setRequiresPayment(true);
        response.setCheckoutUrl(checkoutUrl);
        return response;
    }

    /**
     * Autentica un usuario existente y genera un JWT si las credenciales son válidas.
     * 
     * @param request Credenciales del usuario (email y password).
     * @return AuthResponse con el token generado y datos básicos del usuario.
     * @throws IllegalArgumentException Si el usuario no existe o el email no ha sido confirmado (para clientes).
     * @throws org.springframework.security.core.AuthenticationException Si la contraseña es incorrecta.
     */
    public AuthResponse login(LoginRequest request) {
        // Primero verificamos si el usuario existe y si su email está confirmado
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (!Boolean.TRUE.equals(user.getEmailConfirmed())) {
            throw new IllegalArgumentException("Debes activar tu cuenta confirmando el correo electrónico que te hemos enviado.");
        }

        // Si está confirmado, procedemos a autenticar
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

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

