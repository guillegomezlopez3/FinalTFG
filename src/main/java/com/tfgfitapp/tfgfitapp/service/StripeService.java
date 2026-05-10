package com.tfgfitapp.tfgfitapp.service;

import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import com.tfgfitapp.tfgfitapp.entity.Trainer;
import com.tfgfitapp.tfgfitapp.entity.User;
import com.tfgfitapp.tfgfitapp.repository.TrainerRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Servicio de integración con la API de Stripe para la gestión de pagos.
 * 
 * Implementa flujos para el cobro de suscripciones anuales a entrenadores y
 * cobros mensuales recurrentes por la gestión de clientes individuales.
 */
@Service
public class StripeService {

    private static final Logger log = LoggerFactory.getLogger(StripeService.class);

    @Value("${stripe.price.trainer-annual}")
    private String trainerAnnualPriceId;

    @Value("${stripe.price.client-monthly}")
    private String clientMonthlyPriceId;

    @Value("${app.base-url:http://localhost:8081}")
    private String baseUrl;

    @Value("${app.trial.days:10}")
    private int trialDays;

    private final TrainerRepository trainerRepository;

    public StripeService(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    /**
     * Crea un cliente en Stripe y genera una sesión de Checkout para la suscripción del entrenador.
     * 
     * @param trainerUser Usuario del entrenador.
     * @param trainer Entidad perfil del entrenador.
     * @return URL de la sesión de Checkout de Stripe.
     */
    @Transactional
    public String createTrainerCheckoutSession(User trainerUser, Trainer trainer) {
        try {
            // 1. Crear Customer en Stripe si no tiene
            String customerId = trainer.getStripeCustomerId();
            if (customerId == null || customerId.isEmpty()) {
                CustomerCreateParams customerParams = CustomerCreateParams.builder()
                        .setEmail(trainerUser.getEmail())
                        .setName(trainerUser.getName())
                        .putMetadata("trainerId", trainer.getId().toString())
                        .putMetadata("userId", trainerUser.getId().toString())
                        .build();
                Customer customer = Customer.create(customerParams);
                customerId = customer.getId();
                trainer.setStripeCustomerId(customerId);
                trainer.setTrialEndsAt(LocalDateTime.now().plusDays(trialDays));
                trainerRepository.save(trainer);
            }


            log.info("Usando Stripe Payment Link estático para trainer: https://buy.stripe.com/test_8x26oHczy18D7MM4ll5Ne00");
            return "https://buy.stripe.com/test_8x26oHczy18D7MM4ll5Ne00";
        } catch (Exception e) {
            log.error("Error al redirigir a Stripe: {}", e.getMessage());
            throw new RuntimeException("Error al procesar el pago. Inténtelo de nuevo.", e);
        }
    }

    /**
     * Crea una sesión de pago para el cobro mensual por un nuevo cliente asignado.
     * 
     * @param trainerUser Usuario del entrenador.
     * @param trainer Entidad perfil del entrenador.
     * @param clientId ID del cliente por el que se cobra.
     * @return URL de la sesión de Checkout de Stripe.
     */
    @Transactional
    public String createClientPaymentSession(User trainerUser, Trainer trainer, Long clientId) {
        try {
            String customerId = trainer.getStripeCustomerId();
            if (customerId == null || customerId.isEmpty()) {
                log.info("Creando Stripe Customer bajo demanda para trainer: {}", trainerUser.getEmail());
                CustomerCreateParams customerParams = CustomerCreateParams.builder()
                        .setEmail(trainerUser.getEmail())
                        .setName(trainerUser.getName())
                        .putMetadata("trainerId", trainer.getId().toString())
                        .putMetadata("userId", trainerUser.getId().toString())
                        .build();
                Customer customer = Customer.create(customerParams);
                customerId = customer.getId();
                trainer.setStripeCustomerId(customerId);
                trainerRepository.save(trainer);
            }

            if (clientMonthlyPriceId == null || clientMonthlyPriceId.contains("PLACEHOLDER")) {
                throw new IllegalStateException("El sistema de pagos no está configurado correctamente (Price ID faltante).");
            }


            log.info("Usando Stripe Payment Link estático para cliente: https://buy.stripe.com/test_28E8wP6ba4kP5EE4ll5Ne01");
            return "https://buy.stripe.com/test_28E8wP6ba4kP5EE4ll5Ne01";
        } catch (Exception e) {
            log.error("Error al redirigir a Stripe: {}", e.getMessage());
            throw new RuntimeException("Error al procesar el pago del cliente.", e);
        }
    }

    /**
     * Verifica si el entrenador tiene una suscripción activa o se encuentra en periodo de prueba.
     * 
     * @param trainer Perfil del entrenador.
     * @return true si tiene acceso permitido al sistema; false en caso contrario.
     */
    public boolean isTrainerSubscriptionActive(Trainer trainer) {
        // En periodo de prueba
        if (trainer.getTrialEndsAt() != null && trainer.getTrialEndsAt().isAfter(LocalDateTime.now())) {
            return true;
        }
        // Suscripción activa
        return Boolean.TRUE.equals(trainer.getSubscriptionActive());
    }
}
