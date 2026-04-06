# Resumen del Proyecto y Contexto Actual (TFGFitApp)

Este documento sirve como un estado guardado para que cualquier asistente de IA (o tú mismo) comprenda instantáneamente el contexto del proyecto al iniciar una nueva conversación.

## Descripción General
**Nombre:** Gestor de Clientes para Entrenadores Personales (TFGFitApp)
**Stack:** Java 21, Spring Boot 3.4.5, Spring Data JPA, Spring Security (JWT), MySQL, Thymeleaf, JavaScript (Fetch API + Chart.js + SweetAlert2).

## Arquitectura y Entidades (Modelado de Datos)
El modelo de datos y relaciones de JPA están 100% implementados (evitando bucles infinitos de serialización e implementando los DTOs adecuados):
1. **User (Usuario Base):** `id, name, email, password, role, active`.
2. **Trainer (Entrenador):** 1:1 con `User`. Tiene una lista 1:N de sus `Client`.
3. **Client (Cliente):** 1:1 con `User` y N:1 con `Trainer`. (Guarda datos físicos: altura, peso, nivel, alergias, lesiones).
4. **Diet & DietMeal:** Un Entrenador asigna una Dieta (con múltiples comidas) a un Cliente. Cada comida tiene calorías y detalles.
5. **WorkoutPlan, WorkoutDay & Exercise:** Un Entrenador asigna un plan con `n` días. Cada día tiene `m` ejercicios (series, repeticiones, descansos).
6. **ProgressRecord:** Los clientes y entrenadores pueden registrar y visualizar una evolución histórica de su peso y porcentaje de grasa.

## Modelo de Seguridad e Interacción
- **Roles:** `ADMIN`, `TRAINER`, `CLIENT`.
- **Backend (API REST):** Protegido mediante un `JwtAuthenticationFilter`. Las llamadas esperan `Authorization: Bearer <token>`.
- **Control Securizado (Autorización fina):** Las operaciones de los servicios incluyen `AccessDeniedException` si un Entrenador intenta acceder a la dieta de un cliente que no es suyo, o si un Cliente entra a otro perfil.
- **Frontend (Thymeleaf + AJAX):** Las vistas no se recargan tradicionalmente con sesiones en Spring; consumen la API usando el token JWT guardado en `localStorage`. Archivo vital: `app.js` encapsula las llamadas (`apiFetch()`).

## ¿Qué se ha implementado hasta ahora?
✅ **Fase 1: Configuración Core y Seguridad.**
- Base de datos MySQL con repositorios `JpaRepository`.
- Controladores `AuthController` y filtros configurados.
- Creación automática del perfil `Trainer` o `Client` cuando un `User` se registra.

✅ **Fase 2 y 3: Logica CRUD Completa.**
- `ClientController`: El Trainer puede crear/añadir y desactivar lógicamente (soft-delete) clientes asociados a su cuenta.
- `DietController`: Asignación de dietas y CRUD de comidas (`DietMeal`).
- `WorkoutPlanController`: Construcción en cascada de planes, días y ejercicios.
- `ProgressRecordController`: Obtención de métricas evolutivas y exportación a CSV.

✅ **Fase Frontend (UI/UX).**
- Sistema minimalista con variables CCS (naranja y colores oscuros / dashboard estético).
- Plantillas dinámicas de Thymeleaf y Bootstrap 5 integradas con `app.js`: `dashboard.html`, `clients.html`, `diets.html`, `workouts.html`, `progress.html`, `profile.html`.
- **Toques visuales implementados:** Uso de modales asíncronos para todas las gestiones, alertas mediante SweetAlert2 para conformaciones e informaciones visuales, y gráficos evolutivos en tiempo real con `Chart.js` en la pantalla de progreso físico.

## Siguientes Pasos (A continuar cuando se decida)
1. **Pruebas Integrales de Usuario:** Ejecutar la base de datos, levantar Tomcat y navegar por toda la aplicación desde el navegador imitando el registro de un nuevo entrenador que añade un cliente y su dieta. 
2. **Afinado de Formularios Complejos:** Posibles refactorizaciones o mejoras en la introducción de ejercicios (Ej: clonar un día de entrenamiento recurrente).
3. **Imprimir Dietas / PDF:** Quizás la aplicación puede beneficiarse (y subir de nota para el TFG) exportando rutinas o dietas a formato PDF.
4. **Despliegue (Docker/Nube):** Crear configurables de Docker definitivos para entregar el proyecto con un solo comando.
