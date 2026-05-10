package com.tfgfitapp.tfgfitapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador MVC para servir las páginas HTML del sistema.
 * 
 * Gestiona la navegación principal, incluyendo la landing page, login, registro
 * y todas las vistas del dashboard privado.
 */
@Controller
public class WebController {

    /** @return La página de inicio (Landing Page). */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /** @return La página de inicio de sesión. */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /** @return La página de registro de nuevos usuarios. */
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    /** @return La página de confirmación de correo electrónico. */
    @GetMapping("/confirm-email")
    public String confirmEmail() {
        return "confirm-email";
    }

    @GetMapping("/payment-success")
    public String paymentSuccess() {
        return "payment-success";
    }

    @GetMapping("/payment-cancel")
    public String paymentCancel() {
        return "payment-cancel";
    }

    /** @return El panel principal (Dashboard). */
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

    @GetMapping("/dashboard/workout-view")
    public String workoutView() {
        return "dashboard/workout-view";
    }

    @GetMapping("/dashboard/diet-view")
    public String dietView() {
        return "dashboard/diet-view";
    }
}

