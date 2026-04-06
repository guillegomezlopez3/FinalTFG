package com.tfgfitapp.tfgfitapp.dto;

import com.tfgfitapp.tfgfitapp.enumeration.ClientLevel;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para datos de un cliente.
 * Evita exponer la entidad JPA directamente y previene bucles de serialización.
 */
@Data
@Builder
public class ClientResponse {

    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;

    private Long trainerId;
    private String trainerName;

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
    private LocalDateTime createdAt;
}

