package com.tfgfitapp.tfgfitapp.dto;

import com.tfgfitapp.tfgfitapp.enumeration.ClientLevel;

import java.math.BigDecimal;

/**
 * DTO para actualizar el perfil de un cliente.
 * El trainer y el user no se modifican desde aquí.
 * Todos los campos son opcionales (se actualizan solo los no nulos).
 */
public class ClientUpdateRequest {

    public ClientUpdateRequest() {}

    private Integer age;
    private String gender;
    private BigDecimal height;
    private BigDecimal weight;
    private String goal;
    private ClientLevel level;
    private String injuries;
    private String allergies;
    private String notes;
    private Boolean active;

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public BigDecimal getHeight() { return height; }
    public void setHeight(BigDecimal height) { this.height = height; }
    public BigDecimal getWeight() { return weight; }
    public void setWeight(BigDecimal weight) { this.weight = weight; }
    public String getGoal() { return goal; }
    public void setGoal(String goal) { this.goal = goal; }
    public ClientLevel getLevel() { return level; }
    public void setLevel(ClientLevel level) { this.level = level; }
    public String getInjuries() { return injuries; }
    public void setInjuries(String injuries) { this.injuries = injuries; }
    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}

