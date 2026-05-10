package com.tfgfitapp.tfgfitapp.dto;

/**
 * DTO para la actualización de los datos profesionales del perfil de un entrenador.
 */
public class TrainerUpdateRequest {

    public TrainerUpdateRequest() {}

    private String phone;
    private String specialty;
    private String description;

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

