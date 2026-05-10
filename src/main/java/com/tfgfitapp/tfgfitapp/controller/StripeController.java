package com.tfgfitapp.tfgfitapp.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.tfgfitapp.tfgfitapp.entity.Trainer;
import com.tfgfitapp.tfgfitapp.entity.User;
import com.tfgfitapp.tfgfitapp.exception.ResourceNotFoundException;
import com.tfgfitapp.tfgfitapp.repository.TrainerRepository;
import com.tfgfitapp.tfgfitapp.service.StripeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador para la integración con Stripe para pagos y suscripciones.
 * 
 * Gestiona la creación de sesiones de pago para entrenadores y clientes,
 * la activación manual de perfiles y la recepción de eventos mediante webhooks.
 */
@RestController
@RequestMapping("/api/stripe")
public class StripeController {

    private static final Logger log = LoggerFactory.getLogger(StripeController.class);

    private final StripeService stripeService;
    private final TrainerRepository trainerRepository;

    @Value("${stripe.webhook.secret:}")
    private String webhookSecret;

    public StripeController(StripeService stripeService, TrainerRepository trainerRepository) {
        this.stripeService = stripeService;
        this.trainerRepository = trainerRepository;
    }

    /**
     * Crea una sesión de Stripe Checkout para la suscripción de un entrenador.
     * 
     * @param currentUser Entrenador que desea suscribirse.
     * @return URL de redirección a la página de pago de Stripe.
     */
    @PostMapping("/checkout/trainer")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<Map<String, String>> createTrainerCheckout(@AuthenticationPrincipal User currentUser) {
        Trainer trainer = trainerRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de entrenador no encontrado"));
        String checkoutUrl = stripeService.createTrainerCheckoutSession(currentUser, trainer);
        return ResponseEntity.ok(Map.of("checkoutUrl", checkoutUrl));
    }

    /**
     * Crea una sesión de pago para el alta de un nuevo cliente bajo un entrenador.
     * 
     * @param clientId ID del cliente.
     * @param currentUser Entrenador autenticado.
     * @return URL de pago de Stripe.
     */
    @PostMapping("/checkout/client/{clientId}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<Map<String, String>> createClientCheckout(
            @PathVariable Long clientId,
            @AuthenticationPrincipal User currentUser) {
        Trainer trainer = trainerRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de entrenador no encontrado"));
        String checkoutUrl = stripeService.createClientPaymentSession(currentUser, trainer, clientId);
        return ResponseEntity.ok(Map.of("checkoutUrl", checkoutUrl));
    }

    @PostMapping("/activate-trainer")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<Map<String, String>> activateTrainer(@AuthenticationPrincipal User currentUser) {
        Trainer trainer = trainerRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de entrenador no encontrado"));
        trainer.setSubscriptionActive(true);
        trainerRepository.save(trainer);
        return ResponseEntity.ok(Map.of("message", "Suscripción activada con éxito"));
    }

    /**
     * Webhook de Stripe para procesar eventos de pago.
     * Este endpoint es público (sin JWT) y se valida con la firma de Stripe.
     */
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader(value = "Stripe-Signature", required = false) String sigHeader) {

        Event event;

        // Validar firma si hay webhook secret configurado
        if (webhookSecret != null && !webhookSecret.isEmpty() && sigHeader != null) {
            try {
                event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
            } catch (SignatureVerificationException e) {
                log.warn("Webhook: firma de Stripe inválida");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Firma inválida");
            } catch (Exception e) {
                log.error("Webhook: error al verificar evento: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al verificar");
            }
        } else {
            // Sin webhook secret → parsear directamente (solo para desarrollo)
            event = Event.GSON.fromJson(payload, Event.class);
        }

        log.info("Webhook Stripe recibido: tipo={}", event.getType());

        // Procesar eventos relevantes
        switch (event.getType()) {
            case "checkout.session.completed" -> {
                EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
                if (deserializer.getObject().isPresent()) {
                    StripeObject obj = deserializer.getObject().get();
                    if (obj instanceof Session session) {
                        handleCheckoutCompleted(session);
                    }
                }
            }
            case "customer.subscription.deleted" -> {
                log.info("Suscripción cancelada");
                // Aquí se podría desactivar la suscripción del trainer
            }
            case "invoice.paid" -> {
                log.info("Factura pagada");
            }
            default -> log.debug("Evento Stripe no procesado: {}", event.getType());
        }

        return ResponseEntity.ok("OK");
    }

    private void handleCheckoutCompleted(Session session) {
        String customerId = session.getCustomer();
        String subscriptionId = session.getSubscription();
        log.info("Checkout completado: customer={}, subscription={}", customerId, subscriptionId);

        if (customerId != null) {
            trainerRepository.findByStripeCustomerId(customerId).ifPresent(trainer -> {
                trainer.setSubscriptionActive(true);
                if (subscriptionId != null) {
                    trainer.setStripeSubscriptionId(subscriptionId);
                }
                trainerRepository.save(trainer);
                log.info("Suscripción activada para trainer ID: {}", trainer.getId());
            });
        }
    }
    
    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    public ResponseEntity<Map<String, String>> handleStripeErrors(Exception e) {
        return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }
}
