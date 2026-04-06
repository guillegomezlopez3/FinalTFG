# TFGFitApp — Gestor de Clientes para Entrenadores Personales

> **Fecha de creacion:** Marzo 2026
> **Ultima actualizacion:** Marzo 2026 — Fase 2 completada
> **Tecnologias:** Java 21 · Spring Boot 3.4.5 · Spring Security · JWT · Spring Data JPA · MySQL · Lombok

---

## Descripcion general

Aplicacion backend REST para la gestion de clientes de entrenadores personales. Permite a los entrenadores registrarse, gestionar sus clientes, crear dietas y planes de entrenamiento personalizados, y hacer seguimiento del progreso fisico de cada cliente.

---

## Roles del sistema

| Rol | Descripcion |
|-----|-------------|
| `ADMIN` | Gestiona la plataforma a nivel global. Se crea automaticamente al arrancar (DataInitializer). |
| `TRAINER` | Entrenador personal. Gestiona sus propios clientes, dietas y entrenamientos. |
| `CLIENT` | Cliente. Solo puede visualizar su propia informacion y registrar su progreso. |

---

## Estructura de paquetes

```
com.tfgfitapp.tfgfitapp/
├── TfgFitAppApplication.java
├── config/
│   ├── SecurityConfig.java          ← Spring Security + BCrypt + Sesion Stateless
│   └── DataInitializer.java         ← Crea el ADMIN por defecto al arrancar (Fase 2)
├── controller/
│   ├── AuthController.java          ← POST /api/auth/register y /api/auth/login
│   ├── UserController.java          ← GET /api/me (Fase 2)
│   ├── ClientController.java        ← CRUD clientes (Fase 2)
│   ├── DietController.java          ← CRUD dietas + comidas (Fase 2)
│   ├── WorkoutPlanController.java   ← CRUD planes + dias + ejercicios (Fase 2)
│   └── ProgressRecordController.java← CRUD registros de progreso (Fase 2)
├── dto/
│   ├── AuthResponse.java
│   ├── LoginRequest.java
│   ├── RegisterRequest.java
│   ├── UserProfileResponse.java     ← Perfil completo del usuario autenticado (Fase 2)
│   ├── ClientResponse.java          ← (Fase 2)
│   ├── ClientUpdateRequest.java     ← (Fase 2)
│   ├── AssignTrainerRequest.java    ← (Fase 2)
│   ├── DietRequest.java             ← (Fase 2)
│   ├── DietResponse.java            ← (Fase 2)
│   ├── DietMealRequest.java         ← (Fase 2)
│   ├── DietMealResponse.java        ← (Fase 2)
│   ├── WorkoutPlanRequest.java      ← (Fase 2)
│   ├── WorkoutPlanResponse.java     ← (Fase 2)
│   ├── WorkoutDayRequest.java       ← (Fase 2)
│   ├── WorkoutDayResponse.java      ← (Fase 2)
│   ├── ExerciseRequest.java         ← (Fase 2)
│   ├── ExerciseResponse.java        ← (Fase 2)
│   ├── ProgressRecordRequest.java   ← (Fase 2)
│   └── ProgressRecordResponse.java  ← (Fase 2)
├── entity/
│   ├── User.java
│   ├── Trainer.java
│   ├── Client.java
│   ├── Diet.java
│   ├── DietMeal.java
│   ├── WorkoutPlan.java
│   ├── WorkoutDay.java
│   ├── Exercise.java
│   └── ProgressRecord.java
├── enumeration/
│   ├── Role.java
│   ├── ClientLevel.java
│   └── DayOfWeekPlan.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java  ← HTTP 404 personalizado (Fase 2)
├── repository/
│   ├── UserRepository.java
│   ├── TrainerRepository.java
│   ├── ClientRepository.java
│   ├── DietRepository.java
│   ├── DietMealRepository.java
│   ├── WorkoutPlanRepository.java
│   ├── WorkoutDayRepository.java
│   ├── ExerciseRepository.java
│   └── ProgressRecordRepository.java
├── security/
│   ├── JwtService.java
│   ├── JwtAuthenticationFilter.java
│   └── CustomUserDetailsService.java
└── service/
    ├── AuthService.java
    ├── UserService.java             ← (Fase 2)
    ├── ClientService.java           ← (Fase 2)
    ├── DietService.java             ← (Fase 2)
    ├── WorkoutPlanService.java      ← (Fase 2)
    └── ProgressRecordService.java   ← (Fase 2)
```

---

## Modelo de datos

### Entidades y relaciones

```
User ──1:1──► Trainer ──1:N──► Client
                │                 │
                │                 ├──1:N──► Diet ──1:N──► DietMeal
                │                 │
                │                 ├──1:N──► WorkoutPlan ──1:N──► WorkoutDay ──1:N──► Exercise
                │                 │
                └──1:N──► Diet    └──1:N──► ProgressRecord
                └──1:N──► WorkoutPlan
```

### Descripcion de entidades

| Entidad | Campos destacados |
|---------|-------------------|
| `User` | id, name, email, password (BCrypt), role, active, createdAt |
| `Trainer` | id, user(1:1), phone, specialty, description, createdAt |
| `Client` | id, user(1:1), trainer(N:1), age, gender, height, weight, goal, level, injuries, allergies, notes, active, createdAt |
| `Diet` | id, client, trainer, title, description, startDate, endDate, active, createdAt, updatedAt |
| `DietMeal` | id, diet, mealType, mealTime, foods, calories, notes |
| `WorkoutPlan` | id, client, trainer, title, objective, notes, startDate, endDate, active, createdAt, updatedAt |
| `WorkoutDay` | id, workoutPlan, dayOfWeek (enum), focus, notes |
| `Exercise` | id, workoutDay, name, sets, reps (String), restSeconds, durationMinutes, notes |
| `ProgressRecord` | id, client, recordDate, weight, bodyFat, chest, waist, hips, arms, legs, notes, createdAt |

---

## Seguridad

- **Algoritmo JWT:** HS256 con clave secreta configurable en `application.properties`
- **Expiracion del token:** 24 horas (86400000 ms, configurable)
- **Contrasenas:** cifradas con `BCryptPasswordEncoder`
- **Sesion:** completamente stateless (no hay `HttpSession`)
- **Filtro:** `JwtAuthenticationFilter` intercepta cada request y valida el token
- **Control de roles:** `@EnableMethodSecurity` + `@PreAuthorize` en cada endpoint
- **Control de propiedad:** verificacion en cada servicio de que el recurso pertenece al usuario

---

## Mapa completo de endpoints (Fase 1 + Fase 2)

### Publicos (sin JWT)

| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| `POST` | `/api/auth/register` | Registra TRAINER o CLIENT |
| `POST` | `/api/auth/login` | Autentica y devuelve JWT |

### Protegidos (requieren `Authorization: Bearer <token>`)

#### Perfil propio
| Metodo | Endpoint | Roles | Descripcion |
|--------|----------|-------|-------------|
| `GET` | `/api/me` | Todos | Perfil del usuario autenticado con datos de Trainer/Client |

#### Clientes
| Metodo | Endpoint | Roles | Descripcion |
|--------|----------|-------|-------------|
| `GET` | `/api/clients` | TRAINER, ADMIN | Lista paginada de clientes del entrenador. Filtro opcional: `?level=BEGINNER\|INTERMEDIATE\|ADVANCED` |
| `GET` | `/api/clients/{id}` | TRAINER, CLIENT, ADMIN | Detalle de un cliente |
| `PUT` | `/api/clients/{id}` | TRAINER, CLIENT, ADMIN | Actualiza datos del cliente |
| `PUT` | `/api/clients/{id}/trainer` | ADMIN | Asigna un entrenador a un cliente |

#### Dietas
| Metodo | Endpoint | Roles | Descripcion |
|--------|----------|-------|-------------|
| `POST` | `/api/diets` | TRAINER, ADMIN | Crea una dieta para uno de sus clientes |
| `GET` | `/api/diets/client/{clientId}` | TRAINER, CLIENT, ADMIN | Dietas de un cliente |
| `GET` | `/api/diets/{id}` | TRAINER, CLIENT, ADMIN | Detalle de una dieta con sus comidas |
| `PUT` | `/api/diets/{id}` | TRAINER, ADMIN | Actualiza una dieta |
| `DELETE` | `/api/diets/{id}` | TRAINER, ADMIN | Elimina una dieta |
| `POST` | `/api/diets/{dietId}/meals` | TRAINER, ADMIN | Anade una comida a la dieta |
| `PUT` | `/api/diets/meals/{mealId}` | TRAINER, ADMIN | Actualiza una comida |
| `DELETE` | `/api/diets/meals/{mealId}` | TRAINER, ADMIN | Elimina una comida |

#### Planes de entrenamiento
| Metodo | Endpoint | Roles | Descripcion |
|--------|----------|-------|-------------|
| `POST` | `/api/workout-plans` | TRAINER, ADMIN | Crea un plan para uno de sus clientes |
| `GET` | `/api/workout-plans/client/{clientId}` | TRAINER, CLIENT, ADMIN | Planes de un cliente |
| `GET` | `/api/workout-plans/{id}` | TRAINER, CLIENT, ADMIN | Detalle de un plan con dias y ejercicios |
| `PUT` | `/api/workout-plans/{id}` | TRAINER, ADMIN | Actualiza un plan |
| `DELETE` | `/api/workout-plans/{id}` | TRAINER, ADMIN | Elimina un plan |
| `POST` | `/api/workout-plans/{planId}/days` | TRAINER, ADMIN | Anade un dia al plan |
| `PUT` | `/api/workout-plans/days/{dayId}` | TRAINER, ADMIN | Actualiza un dia |
| `DELETE` | `/api/workout-plans/days/{dayId}` | TRAINER, ADMIN | Elimina un dia |
| `POST` | `/api/workout-plans/days/{dayId}/exercises` | TRAINER, ADMIN | Anade un ejercicio al dia |
| `PUT` | `/api/workout-plans/exercises/{exerciseId}` | TRAINER, ADMIN | Actualiza un ejercicio |
| `DELETE` | `/api/workout-plans/exercises/{exerciseId}` | TRAINER, ADMIN | Elimina un ejercicio |

#### Registros de progreso
| Metodo | Endpoint | Roles | Descripcion |
|--------|----------|-------|-------------|
| `POST` | `/api/progress` | CLIENT | El cliente registra sus medidas del dia |
| `GET` | `/api/progress/me` | CLIENT | Historial propio (mas reciente primero) |
| `GET` | `/api/progress/me/export` | CLIENT | Descarga CSV del historial propio |
| `GET` | `/api/progress/client/{clientId}` | TRAINER, ADMIN | Historial de un cliente |
| `GET` | `/api/progress/client/{clientId}/export` | TRAINER, ADMIN | Descarga CSV del historial de un cliente |
| `GET` | `/api/progress/{id}` | CLIENT, TRAINER, ADMIN | Un registro concreto |
| `DELETE` | `/api/progress/{id}` | CLIENT, ADMIN | Elimina un registro |
| `GET` | `/api/progress/client/{clientId}` | TRAINER, ADMIN | Historial de un cliente |
| `GET` | `/api/progress/{id}` | CLIENT, TRAINER, ADMIN | Un registro concreto |
| `DELETE` | `/api/progress/{id}` | CLIENT, ADMIN | Elimina un registro |

---

## Como ejecutar el proyecto

### 1. Prerequisitos
- Java 21
- MySQL corriendo en `localhost:3306`
- Base de datos `personal_trainer_manager` creada con el script SQL

### 2. Configurar `application.properties`
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/personal_trainer_manager?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD
```

### 3. Ejecutar
```bash
./mvnw spring-boot:run
```

El servidor arranca en `http://localhost:8080`.
Al iniciar, **DataInitializer** crea automaticamente el usuario ADMIN si no existe:
- Email: `admin@tfgfitapp.com`
- Password: `Admin1234!`

### 4. Flujo tipico de prueba

#### a) Login como ADMIN y asignar entrenador a cliente
```http
POST /api/auth/login
{ "email": "admin@tfgfitapp.com", "password": "Admin1234!" }

PUT /api/clients/1/trainer          (con token ADMIN)
{ "trainerId": 1 }
```

#### b) TRAINER crea una dieta para su cliente
```http
POST /api/diets                     (con token TRAINER)
{
  "clientId": 1,
  "title": "Dieta de definicion",
  "startDate": "2026-04-01",
  "endDate": "2026-06-30",
  "active": true
}

POST /api/diets/1/meals             (con token TRAINER)
{
  "mealType": "Desayuno",
  "mealTime": "08:00",
  "foods": "Avena con leche desnatada y platano",
  "calories": 350
}
```

#### c) TRAINER crea un plan de entrenamiento
```http
POST /api/workout-plans             (con token TRAINER)
{
  "clientId": 1,
  "title": "Plan Full Body 3 dias",
  "objective": "Hipertrofia muscular moderada"
}

POST /api/workout-plans/1/days      (con token TRAINER)
{ "dayOfWeek": "MONDAY", "focus": "Tren superior" }

POST /api/workout-plans/days/1/exercises   (con token TRAINER)
{
  "name": "Press banca",
  "sets": 4,
  "reps": "8-10",
  "restSeconds": 90
}
```

#### d) CLIENT consulta su informacion y registra progreso
```http
GET /api/me                         (con token CLIENT)
GET /api/diets/client/1             (con token CLIENT)
GET /api/workout-plans/client/1     (con token CLIENT)

POST /api/progress                  (con token CLIENT)
{
  "recordDate": "2026-04-01",
  "weight": 75.5,
  "bodyFat": 18.2,
  "waist": 82.0
}

GET /api/progress/me                (con token CLIENT)
```

---

## Ejemplos de respuesta JSON

### GET /api/me (TRAINER)
```json
{
  "id": 1,
  "name": "Carlos Lopez",
  "email": "carlos@trainer.com",
  "role": "TRAINER",
  "active": true,
  "createdAt": "2026-03-11T10:00:00",
  "trainerId": 1,
  "phone": null,
  "specialty": null,
  "description": null
}
```

### GET /api/diets/1 (con comidas incluidas)
```json
{
  "id": 1,
  "clientId": 1,
  "clientName": "Ana Garcia",
  "trainerId": 1,
  "trainerName": "Carlos Lopez",
  "title": "Dieta de definicion",
  "startDate": "2026-04-01",
  "endDate": "2026-06-30",
  "active": true,
  "meals": [
    {
      "id": 1,
      "dietId": 1,
      "mealType": "Desayuno",
      "mealTime": "08:00",
      "foods": "Avena con leche desnatada y platano",
      "calories": 350
    }
  ]
}
```

---

## Dependencias principales (`pom.xml`)

| Dependencia | Version | Uso |
|-------------|---------|-----|
| `spring-boot-starter-web` | 3.4.5 | API REST |
| `spring-boot-starter-security` | 3.4.5 | Autenticacion y autorizacion |
| `spring-boot-starter-data-jpa` | 3.4.5 | Acceso a base de datos |
| `spring-boot-starter-validation` | 3.4.5 | Validacion de DTOs con Jakarta |
| `mysql-connector-j` | 3.4.5 | Driver MySQL |
| `jjwt-api / jjwt-impl / jjwt-jackson` | 0.12.6 | Generacion y validacion JWT |
| `lombok` | — | Reduccion de boilerplate |

---

## Decisiones de diseno

| Decision | Motivo |
|----------|--------|
| `User` implementa `UserDetails` directamente | Simplifica la arquitectura, evita wrapper innecesario |
| `@JsonIgnore` en relaciones bidireccionales | Evita bucles de serializacion infinitos en Jackson |
| ADMIN creado por `DataInitializer` | Seguro: no hay endpoint publico para crear admins |
| `trainer_id` nullable en `Client` | Permite registrar clientes antes de asignarles entrenador |
| `reps` como `String` en `Exercise` | Soporta rangos como `"8-12"` o `"AMRAP"` |
| Control de acceso en el servicio | Ademas de `@PreAuthorize` por rol, cada servicio verifica la propiedad del recurso |
| DTOs para todas las respuestas | Evita exponer entidades JPA y los problemas de serializacion de Hibernate |
| `findByIdAndTrainerId` en repositories | Consulta que combina busqueda y verificacion de propiedad en una sola query |

---

## Estado del proyecto

### Fase 1 — Completada
- [x] Entidades JPA completas con relaciones
- [x] Enums (Role, ClientLevel, DayOfWeekPlan)
- [x] Seguridad JWT completa (JwtService, JwtAuthenticationFilter, CustomUserDetailsService)
- [x] Endpoints de autenticacion (register, login)
- [x] Control de roles basico

### Fase 2 — Completada
- [x] Endpoint `/api/me` con perfil completo del usuario autenticado
- [x] CRUD de clientes con control de acceso por rol
- [x] CRUD de dietas (TRAINER crea/edita, CLIENT solo lee)
- [x] CRUD de comidas como sub-recurso de dieta
- [x] CRUD de planes de entrenamiento con misma logica de acceso
- [x] CRUD de dias de entrenamiento como sub-recurso del plan
- [x] CRUD de ejercicios como sub-recurso del dia
- [x] Registros de progreso (CLIENT crea los suyos, TRAINER los consulta)
- [x] Asignacion de entrenador a cliente (solo ADMIN)
- [x] Seed automatico del usuario ADMIN al arrancar (DataInitializer)
- [x] `ResourceNotFoundException` con respuesta HTTP 404
- [x] `AccessDeniedException` manejada con HTTP 403

### Fase 3 — Completada
- [x] Paginacion con `Pageable` en listados grandes
- [x] Tests unitarios con Mockito: `ClientServiceTest` (7), `TrainerServiceTest` (7), `WorkoutPlanServiceTest` (12)
- [x] Tests de integracion `@SpringBootTest`: `AuthControllerTest` (8), `TrainerControllerTest` (9), `AdminControllerTest` (12)
- [x] Panel ADMIN: listar todos los trainers, todos los clientes, estadisticas
- [x] Endpoint de actualizacion del perfil del Trainer (especialidad, telefono, descripcion)
- [x] Filtro avanzado por nivel de cliente (`?level=BEGINNER|INTERMEDIATE|ADVANCED`) en `GET /api/clients`
- [x] Filtro por estado activo (`?active=true|false`) en dietas y planes de entrenamiento
- [x] Exportacion CSV del historial de progreso: `GET /api/progress/me/export` y `GET /api/progress/client/{id}/export`

### Fase 4 — Siguientes pasos
- [ ] Notificaciones o alertas (p.ej. cuando el cliente no registra progreso en X dias)
- [ ] Exportacion de datos del cliente en PDF (iText/OpenPDF)

---

## Archivos de configuracion relevantes

| Archivo | Ubicacion |
|---------|-----------|
| `application.properties` | `src/main/resources/` |
| `pom.xml` | Raiz del proyecto |
| Script SQL | Ejecutar manualmente antes de arrancar |
| `documentacion/RESUMEN_PROYECTO.md` | Este archivo |
