package com.tfgfitapp.tfgfitapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Configuración de CORS (Cross-Origin Resource Sharing).
 * 
 * Define las políticas de acceso desde orígenes externos (frontend) a la API.
 * Permite configurar los orígenes permitidos, métodos HTTP, cabeceras y exposición de tokens JWT.
 */
@Configuration
public class CorsConfig {

    @Value("${app.cors.allowed-origins:http://localhost:3000,http://localhost:5173}")
    private String allowedOriginsRaw;

    /**
     * Define el bean de configuración de CORS.
     * 
     * @return Fuente de configuración CORS basada en URLs.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Orígenes permitidos: frontend local y cualquier origen configurado
        List<String> origins = Arrays.asList(allowedOriginsRaw.split(","));
        config.setAllowedOrigins(origins);

        // Métodos HTTP permitidos
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // Cabeceras permitidas (Authorization es necesario para JWT)
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept", "X-Requested-With"));

        // Exponer Authorization en la respuesta para que el frontend pueda leerla
        config.setExposedHeaders(List.of("Authorization"));

        // Permitir cookies / credenciales (necesario para algunos clientes)
        config.setAllowCredentials(true);

        // Cache de preflight durante 1 hora (reduce peticiones OPTIONS)
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }
}
