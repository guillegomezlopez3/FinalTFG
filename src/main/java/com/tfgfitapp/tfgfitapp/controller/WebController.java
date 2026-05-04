package com.tfgfitapp.tfgfitapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador MVC que sirve las páginas Thymeleaf.
 *
 * Estrategia de autenticación en la capa web:
 * - Las páginas públicas (/, /login, /register) no requieren token.
 * - Las páginas privadas (/dashboard/**) son HTML estáticos que, al cargarse,
 *   leen el JWT del localStorage y hacen peticiones AJAX a la API REST.
 * - Si el token no existe o está expirado, el propio JavaScript redirige a /login.
 *
 * Rutas:
 *   GET /            → landing page (index.html)
 *   GET /login       → formulario de login (login.html)
 *   GET /register    → formulario de registro (register.html)
 *   GET /dashboard   → panel principal tras login (dashboard.html)
 *   GET /dashboard/clients   → listado de clientes (clients.html)
 *   GET /dashboard/diets     → listado de dietas (diets.html)
 *   GET /dashboard/workouts        → planes de entrenamiento (workouts.html)
 *   GET /dashboard/workout-builder → constructor interactivo de planes (workout-builder.html)
 *   GET /dashboard/diet-builder    → constructor interactivo de dietas (diet-builder.html)
 *   GET /dashboard/progress        → registros de progreso (progress.html)
 *   GET /dashboard/admin           → panel de administración (admin.html)
 *   GET /dashboard/profile         → perfil del usuario (profile.html)
 */
@Controller
public class WebController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard/dashboard";
    }

    @GetMapping("/dashboard/clients")
    public String clients() {
        return "dashboard/clients";
    }

    @GetMapping("/dashboard/diets")
    public String diets() {
        return "dashboard/diets";
    }

    @GetMapping("/dashboard/workouts")
    public String workouts() {
        return "dashboard/workouts";
    }

    @GetMapping("/dashboard/progress")
    public String progress() {
        return "dashboard/progress";
    }

    @GetMapping("/dashboard/admin")
    public String admin() {
        return "dashboard/admin";
    }

    @GetMapping("/dashboard/profile")
    public String profile() {
        return "dashboard/profile";
    }

    @GetMapping("/dashboard/workout-builder")
    public String workoutBuilder() {
        return "dashboard/workout-builder";
    }

    @GetMapping("/dashboard/diet-builder")
    public String dietBuilder() {
        return "dashboard/diet-builder";
    }
}

