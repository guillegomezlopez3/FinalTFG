package com.tfgfitapp.tfgfitapp.service;

import com.tfgfitapp.tfgfitapp.dto.ChangePasswordRequest;
import com.tfgfitapp.tfgfitapp.dto.UserProfileResponse;
import com.tfgfitapp.tfgfitapp.entity.Trainer;
import com.tfgfitapp.tfgfitapp.entity.User;
import com.tfgfitapp.tfgfitapp.enumeration.Role;
import com.tfgfitapp.tfgfitapp.repository.ClientRepository;
import com.tfgfitapp.tfgfitapp.repository.TrainerRepository;
import com.tfgfitapp.tfgfitapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para operaciones sobre el propio usuario autenticado.
 * Usado por el endpoint GET /api/me.
 */
@Service
public class UserService {

    private final TrainerRepository trainerRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(TrainerRepository trainerRepository, ClientRepository clientRepository,
                       UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.trainerRepository = trainerRepository;
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Devuelve el perfil completo del usuario autenticado,
     * incluyendo datos de su perfil Trainer o Client si aplica.
     */
    @Transactional(readOnly = true)
    public UserProfileResponse getMyProfile(User currentUser) {
        UserProfileResponse response = new UserProfileResponse();
        response.setId(currentUser.getId());
        response.setName(currentUser.getName());
        response.setEmail(currentUser.getEmail());
        response.setRole(currentUser.getRole());
        response.setActive(currentUser.getActive());
        response.setCreatedAt(currentUser.getCreatedAt());
        response.setAvatar(currentUser.getAvatar());

        if (currentUser.getRole() == Role.TRAINER) {
            trainerRepository.findByUserId(currentUser.getId()).ifPresent(trainer -> {
                response.setTrainerId(trainer.getId());
                response.setPhone(trainer.getPhone());
                response.setSpecialty(trainer.getSpecialty());
                response.setDescription(trainer.getDescription());
            });
        } else if (currentUser.getRole() == Role.CLIENT) {
            clientRepository.findByUserId(currentUser.getId()).ifPresent(client -> {
                response.setClientId(client.getId());
                if (client.getTrainer() != null) {
                    Trainer t = client.getTrainer();
                    response.setAssignedTrainerId(t.getId());
                    response.setAssignedTrainerName(t.getUser() != null ? t.getUser().getName() : null);
                }
            });
        }

        return response;
    }

    /**
     * Cambia la contraseña del usuario autenticado.
     */
    @Transactional
    public void changePassword(User currentUser, ChangePasswordRequest request) {
        if (!passwordEncoder.matches(request.getCurrentPassword(), currentUser.getPassword())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }
        currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(currentUser);
    }
}
