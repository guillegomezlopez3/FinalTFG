package com.tfgfitapp.tfgfitapp.service;

import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
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

    @Value("${stripe.api.key}")
    private String apiKey;

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
            if (isStripeConfiguredForCheckout(trainerAnnualPriceId)) {
                String customerId = ensureStripeCustomer(trainerUser, trainer);
                SessionCreateParams params = SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                        .setCustomer(customerId)
                        .addLineItem(SessionCreateParams.LineItem.builder()
                                .setPrice(trainerAnnualPriceId)
                                .setQuantity(1L)
                                .build())
                        .setSuccessUrl(buildAppUrl("/payment-success?flow=trainer"))
                        .setCancelUrl(buildAppUrl("/payment-cancel?flow=trainer"))
                        .build();

                Session session = Session.create(params);
                log.info("Checkout de Stripe creado para trainer {} -> {}", trainerUser.getEmail(), session.getId());
                return session.getUrl();
            } else {
                log.info("Stripe no está completamente configurado. Usando Payment Link estático para trainer.");
                return "https://buy.stripe.com/test_8x26oHczy18D7MM4ll5Ne00";
            }
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
            if (isStripeConfiguredForCheckout(clientMonthlyPriceId)) {
                String customerId = ensureStripeCustomer(trainerUser, trainer);
                SessionCreateParams params = SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                        .setCustomer(customerId)
                        .addLineItem(SessionCreateParams.LineItem.builder()
                                .setPrice(clientMonthlyPriceId)
                                .setQuantity(1L)
                                .build())
                        .setSuccessUrl(buildAppUrl("/payment-success?flow=client&clientId=" + clientId))
                        .setCancelUrl(buildAppUrl("/payment-cancel?flow=client&clientId=" + clientId))
                        .build();

                Session session = Session.create(params);
                log.info("Checkout de Stripe creado para cliente {} -> {}", clientId, session.getId());
                return session.getUrl();
            } else {
                log.info("Stripe no está completamente configurado. Usando Payment Link estático para cliente.");
                return "https://buy.stripe.com/test_28E8wP6ba4kP5EE4ll5Ne01";
            }
        } catch (Exception e) {
            log.error("Error al redirigir a Stripe: {}", e.getMessage());
            throw new RuntimeException("Error al procesar el pago del cliente.", e);
        }
    }

    /**
     * Crea una sesión de Checkout para dar de alta un nuevo cliente tras el pago.
     * Se usa desde el dashboard de clientes antes de guardar el perfil final.
     */
    @Transactional
    public String createNewClientCheckoutSession(User trainerUser, Trainer trainer) {
        try {
            if (isStripeConfiguredForCheckout(clientMonthlyPriceId)) {
                String customerId = ensureStripeCustomer(trainerUser, trainer);
                SessionCreateParams params = SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                        .setCustomer(customerId)
                        .addLineItem(SessionCreateParams.LineItem.builder()
                                .setPrice(clientMonthlyPriceId)
                                .setQuantity(1L)
                                .build())
                        .setSuccessUrl(buildAppUrl("/payment-success?flow=client"))
                        .setCancelUrl(buildAppUrl("/payment-cancel?flow=client"))
                        .build();

                Session session = Session.create(params);
                log.info("Checkout de Stripe creado para nuevo cliente -> {}", session.getId());
                return session.getUrl();
            } else {
                log.info("Stripe no está completamente configurado. Usando Payment Link estático para nuevo cliente.");
                return "https://buy.stripe.com/test_28E8wP6ba4kP5EE4ll5Ne01";
            }
        } catch (Exception e) {
            log.error("Error al crear el checkout del nuevo cliente: {}", e.getMessage());
            throw new RuntimeException("Error al procesar el pago del cliente.", e);
        }
    }

    /**
     * Devuelve el Payment Link estático de Stripe para la suscripción mensual de clientes.
     * 
     * Los clientes deben pagar esta suscripción para acceder a la app tras el alta.
     * 
     * @return URL del Payment Link de Stripe para clientes.
     */
    public String getClientSubscriptionPaymentLink() {
        return "https://buy.stripe.com/test_8x24gz9nmdVpc321995Ne02";
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

    private String ensureStripeCustomer(User trainerUser, Trainer trainer) throws Exception {
        String customerId = trainer.getStripeCustomerId();
        if (customerId != null && !customerId.isBlank()) {
            return customerId;
        }

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
        return customerId;
    }

    private boolean isStripeConfiguredForCheckout(String priceId) {
        return apiKey != null && !apiKey.isBlank()
                && !apiKey.contains("MOCK")
                && !apiKey.contains("AQUI")
                && !apiKey.contains("*")
                && priceId != null && !priceId.isBlank()
                && !priceId.contains("MOCK")
                && !priceId.contains("PLACEHOLDER");
    }

    private String buildAppUrl(String path) {
        String normalizedBaseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        return normalizedBaseUrl + path;
    }
}
