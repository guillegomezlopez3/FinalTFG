package com.tfgfitapp.tfgfitapp.service;

import com.tfgfitapp.tfgfitapp.dto.AdminStatsResponse;
import com.tfgfitapp.tfgfitapp.dto.ClientResponse;
import com.tfgfitapp.tfgfitapp.dto.PageResponse;
import com.tfgfitapp.tfgfitapp.entity.User;
import com.tfgfitapp.tfgfitapp.exception.ResourceNotFoundException;
import com.tfgfitapp.tfgfitapp.repository.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio exclusivo para el rol de Administrador.
 * 
 * Proporciona lógica de negocio para la consulta de estadísticas globales del sistema,
 * gestión de todos los clientes registrados y control de estado (activación/desactivación) de usuarios.
 */
@Service
public class AdminService {

    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;
    private final ClientRepository clientRepository;
    private final DietRepository dietRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final ProgressRecordRepository progressRecordRepository;
    private final ClientService clientService;

    public AdminService(UserRepository userRepository, TrainerRepository trainerRepository,
                        ClientRepository clientRepository, DietRepository dietRepository,
                        WorkoutPlanRepository workoutPlanRepository,
                        ProgressRecordRepository progressRecordRepository,
                        ClientService clientService) {
        this.userRepository = userRepository;
        this.trainerRepository = trainerRepository;
        this.clientRepository = clientRepository;
        this.dietRepository = dietRepository;
        this.workoutPlanRepository = workoutPlanRepository;
        this.progressRecordRepository = progressRecordRepository;
        this.clientService = clientService;
    }

    /**
     * Obtiene estadísticas agregadas del sistema para el panel de administración.
     * 
     * @return Objeto con contadores de usuarios, entrenadores, clientes y planes.
     */
    @Transactional(readOnly = true)
    public AdminStatsResponse getStats() {
        long totalClients = clientRepository.count();
        long activeClients = clientRepository.countByActive(true);

        AdminStatsResponse stats = new AdminStatsResponse();
        stats.setTotalUsers(userRepository.count());
        stats.setTotalTrainers(trainerRepository.count());
        stats.setTotalClients(totalClients);
        stats.setActiveClients(activeClients);
        stats.setInactiveClients(totalClients - activeClients);
        stats.setTotalDiets(dietRepository.count());
        stats.setActiveDiets(dietRepository.countByActive(true));
        stats.setTotalWorkoutPlans(workoutPlanRepository.count());
        stats.setActiveWorkoutPlans(workoutPlanRepository.countByActive(true));
        stats.setTotalProgressRecords(progressRecordRepository.count());
        return stats;
    }

    /**
     * Obtiene una lista paginada de todos los clientes en la plataforma.
     * 
     * @param pageable Configuración de paginación y ordenamiento.
     * @return Respuesta paginada con la información de los clientes.
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
