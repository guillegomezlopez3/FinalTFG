package com.tfgfitapp.tfgfitapp.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Stripe.
 * 
 * Se encarga de inicializar la clave de API de Stripe al arrancar la aplicación
 * para permitir la comunicación con sus servicios de pago.
 */
@Configuration
public class StripeConfig {

    @Value("${stripe.api.key}")
    private String apiKey;

    /**
     * Inicializa la configuración de Stripe utilizando la clave definida
     * en las propiedades de la aplicación.
     */
    @PostConstruct
    public void init() {
        Stripe.apiKey = apiKey;
    }
}
