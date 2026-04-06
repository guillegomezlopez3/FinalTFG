package com.tfgfitapp.tfgfitapp.service;

import com.tfgfitapp.tfgfitapp.dto.UserProfileResponse;
import com.tfgfitapp.tfgfitapp.entity.Trainer;
import com.tfgfitapp.tfgfitapp.entity.User;
import com.tfgfitapp.tfgfitapp.enumeration.Role;
import com.tfgfitapp.tfgfitapp.repository.ClientRepository;
import com.tfgfitapp.tfgfitapp.repository.TrainerRepository;
import com.tfgfitapp.tfgfitapp.repository.UserRepository;
import com.tfgfitapp.tfgfitapp.dto.ChangePasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para operaciones sobre el propio usuario autenticado.
 * Usado por el endpoint GET /api/me.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final TrainerRepository trainerRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Devuelve el perfil completo del usuario autenticado,
     * incluyendo datos de su perfil Trainer o Client si aplica.
     */
    @Transactional(readOnly = true)
    public UserProfileResponse getMyProfile(User currentUser) {
        UserProfileResponse.UserProfileResponseBuilder builder = UserProfileResponse.builder()
                .id(currentUser.getId())
                .name(currentUser.getName())
                .email(currentUser.getEmail())
                .role(currentUser.getRole())
                .active(currentUser.getActive())
                .createdAt(currentUser.getCreatedAt());

        if (currentUser.getRole() == Role.TRAINER) {
            trainerRepository.findByUserId(currentUser.getId()).ifPresent(trainer -> {
                builder.trainerId(trainer.getId())
                        .phone(trainer.getPhone())
                        .specialty(trainer.getSpecialty())
                        .description(trainer.getDescription());
            });
        } else if (currentUser.getRole() == Role.CLIENT) {
            clientRepository.findByUserId(currentUser.getId()).ifPresent(client -> {
                builder.clientId(client.getId());
                if (client.getTrainer() != null) {
                    Trainer t = client.getTrainer();
                    builder.assignedTrainerId(t.getId())
                            .assignedTrainerName(t.getUser() != null ? t.getUser().getName() : null);
                }
            });
        }

        return builder.build();
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


