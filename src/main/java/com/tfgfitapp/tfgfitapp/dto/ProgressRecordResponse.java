package com.tfgfitapp.tfgfitapp.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para un registro de progreso físico.
 */
@Data
@Builder
public class ProgressRecordResponse {

    private Long id;
    private Long clientId;
    private String clientName;

    private LocalDate recordDate;
    private BigDecimal weight;
    private BigDecimal bodyFat;
    private BigDecimal chest;
    private BigDecimal waist;
    private BigDecimal hips;
    private BigDecimal arms;
    private BigDecimal legs;
    private String notes;

    private LocalDateTime createdAt;
}

