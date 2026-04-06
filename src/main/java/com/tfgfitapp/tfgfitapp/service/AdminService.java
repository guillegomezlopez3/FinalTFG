package com.tfgfitapp.tfgfitapp.service;

import com.tfgfitapp.tfgfitapp.dto.AdminStatsResponse;
import com.tfgfitapp.tfgfitapp.dto.ClientResponse;
import com.tfgfitapp.tfgfitapp.dto.PageResponse;
import com.tfgfitapp.tfgfitapp.entity.User;
import com.tfgfitapp.tfgfitapp.exception.ResourceNotFoundException;
import com.tfgfitapp.tfgfitapp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio exclusivo para el ADMIN.
 * Proporciona estadisticas globales, listados paginados y gestion de usuarios.
 */
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;
    private final ClientRepository clientRepository;
    private final DietRepository dietRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final ProgressRecordRepository progressRecordRepository;
    private final ClientService clientService;

    /**
     * Devuelve estadisticas globales del sistema.
     */
    @Transactional(readOnly = true)
    public AdminStatsResponse getStats() {
        long totalClients = clientRepository.count();
        long activeClients = clientRepository.countByActive(true);

        return AdminStatsResponse.builder()
                .totalUsers(userRepository.count())
                .totalTrainers(trainerRepository.count())
                .totalClients(totalClients)
                .activeClients(activeClients)
                .inactiveClients(totalClients - activeClients)
                .totalDiets(dietRepository.count())
                .activeDiets(dietRepository.countByActive(true))
                .totalWorkoutPlans(workoutPlanRepository.count())
                .activeWorkoutPlans(workoutPlanRepository.countByActive(true))
                .totalProgressRecords(progressRecordRepository.count())
                .build();
    }

    /**
     * Lista paginada de todos los clientes del sistema.
     * Soporta ?page=0&size=10&sort=createdAt,desc
     */
    @Transactional(readOnly = true)
    public PageResponse<ClientResponse> getAllClients(Pageable pageable) {
        return PageResponse.from(clientRepository.findAll(pageable).map(clientService::toResponse));
    }

    /**
     * Activa o desactiva un usuario por su ID.
     * Util para suspender cuentas sin eliminarlas.
     */
    @Transactional
    public void toggleUserActive(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + userId));
        
        boolean newStatus = !user.getActive();
        user.setActive(newStatus);
        userRepository.save(user);

        // Si es cliente, sincronizar el estado activo en su entidad Client
        if (user.getRole() == com.tfgfitapp.tfgfitapp.enumeration.Role.CLIENT) {
            clientRepository.findByUserId(userId).ifPresent(client -> {
                client.setActive(newStatus);
                clientRepository.save(client);
            });
        }
    }
}
