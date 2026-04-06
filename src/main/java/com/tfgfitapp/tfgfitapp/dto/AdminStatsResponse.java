package com.tfgfitapp.tfgfitapp.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO de estadisticas globales del sistema para el panel de ADMIN.
 */
@Data
@Builder
public class AdminStatsResponse {

    private long totalUsers;
    private long totalTrainers;
    private long totalClients;
    private long activeClients;
    private long inactiveClients;
    private long totalDiets;
    private long activeDiets;
    private long totalWorkoutPlans;
    private long activeWorkoutPlans;
    private long totalProgressRecords;
}

