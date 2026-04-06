package com.tfgfitapp.tfgfitapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicación TFGFitApp.
 * Gestor de Clientes para Entrenadores Personales.
 *
 * Arquitectura:
 *   - entity       -> Entidades JPA
 *   - enumeration  -> Enums del dominio
 *   - repository   -> Spring Data JPA repositories
 *   - service      -> Lógica de negocio
 *   - controller   -> Endpoints REST
 *   - dto          -> Objetos de transferencia de datos
 *   - security     -> JWT y Spring Security
 *   - config       -> Configuración de beans
 *   - exception    -> Manejo global de errores
 */
@SpringBootApplication
public class TfgFitAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(TfgFitAppApplication.class, args);
    }
}
