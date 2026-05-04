package com.tfgfitapp.tfgfitapp.dto;

/**
 * DTO de estadisticas globales del sistema para el panel de ADMIN.
 */
public class AdminStatsResponse {

    public AdminStatsResponse() {}

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

    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
    public long getTotalTrainers() { return totalTrainers; }
    public void setTotalTrainers(long totalTrainers) { this.totalTrainers = totalTrainers; }
    public long getTotalClients() { return totalClients; }
    public void setTotalClients(long totalClients) { this.totalClients = totalClients; }
    public long getActiveClients() { return activeClients; }
    public void setActiveClients(long activeClients) { this.activeClients = activeClients; }
    public long getInactiveClients() { return inactiveClients; }
    public void setInactiveClients(long inactiveClients) { this.inactiveClients = inactiveClients; }
    public long getTotalDiets() { return totalDiets; }
    public void setTotalDiets(long totalDiets) { this.totalDiets = totalDiets; }
    public long getActiveDiets() { return activeDiets; }
    public void setActiveDiets(long activeDiets) { this.activeDiets = activeDiets; }
    public long getTotalWorkoutPlans() { return totalWorkoutPlans; }
    public void setTotalWorkoutPlans(long totalWorkoutPlans) { this.totalWorkoutPlans = totalWorkoutPlans; }
    public long getActiveWorkoutPlans() { return activeWorkoutPlans; }
    public void setActiveWorkoutPlans(long activeWorkoutPlans) { this.activeWorkoutPlans = activeWorkoutPlans; }
    public long getTotalProgressRecords() { return totalProgressRecords; }
    public void setTotalProgressRecords(long totalProgressRecords) { this.totalProgressRecords = totalProgressRecords; }
}

