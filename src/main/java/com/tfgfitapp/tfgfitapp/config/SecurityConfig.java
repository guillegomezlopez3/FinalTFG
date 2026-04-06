package com.tfgfitapp.tfgfitapp.config;

import com.tfgfitapp.tfgfitapp.security.CustomUserDetailsService;
import com.tfgfitapp.tfgfitapp.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Configuración central de Spring Security.
 *
 * - CSRF desactivado: la app es REST stateless con JWT, no usa sesiones ni cookies de sesión.
 * - STATELESS: no se crea HttpSession, toda la autenticación va por JWT en cada request.
 * - @EnableMethodSecurity: permite usar @PreAuthorize en los controladores/servicios
 *   para el control fino de roles (ADMIN, TRAINER, CLIENT).
 *
 * Rutas web (Thymeleaf):
 * - /, /login, /register y recursos estáticos son públicos.
 * - /dashboard/** y el resto de rutas web requieren autenticación (verificada
 *   en el propio controller via JS + JWT en localStorage).
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                // ── Endpoints públicos de la API ──────────────────────────
                .requestMatchers("/api/auth/**").permitAll()
                // ── Páginas web públicas (Thymeleaf) ─────────────────────
                .requestMatchers("/", "/login", "/register").permitAll()
                // ── Panel privado Thymeleaf: la autenticación se gestiona
                //    client-side (JWT en localStorage + JS). La seguridad
                //    real está en cada endpoint /api/** que exige JWT.
                .requestMatchers("/dashboard", "/dashboard/**").permitAll()
                // ── Recursos estáticos y WebJars ──────────────────────────
                .requestMatchers(
                    "/css/**", "/js/**", "/images/**", "/favicon.ico",
                    "/webjars/**", "/error"
                ).permitAll()
                // ── Los endpoints de la API sí requieren autenticación JWT ─
                .anyRequest().authenticated()
            )
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
