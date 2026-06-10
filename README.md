[README_FINALTFG.md](https://github.com/user-attachments/files/28792045/README_FINALTFG.md)
<div align="center">

# 💪 LevelUp
### Plataforma integral de gestión fitness

![Status](https://img.shields.io/badge/Estado-Completado-brightgreen?style=for-the-badge)
![Java](https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Flutter](https://img.shields.io/badge/Flutter_3-02569B?style=for-the-badge&logo=flutter&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Stripe](https://img.shields.io/badge/Stripe-635BFF?style=for-the-badge&logo=stripe&logoColor=white)

**LevelUp** conecta entrenadores personales con sus clientes en una sola plataforma: panel web para entrenadores, app móvil para clientes y una API REST como núcleo de todo el sistema.

[🌐 Ver demo en vivo](https://finaltfglevelup.onrender.com/) &nbsp;·&nbsp; [📱 App móvil](#app-móvil-flutter) &nbsp;·&nbsp; [🚀 Instalación](#instalación)

</div>

---

## 🔑 Acceso a la demo

La plataforma está desplegada y disponible para probarse:

| Rol | Email | Contraseña |
|---|---|---|
| 👤 Cliente | juan.perez@example.com | password |
| 🏋️ Entrenador | carlos.ruiz@tfgfitapp.com | password |

> La demo corre en Render (plan gratuito). Si tarda en cargar, espera unos segundos a que el servidor arranque.

---

## ✨ Funcionalidades

### Para entrenadores — Panel web
- 👥 **Gestión de clientes** — alta, seguimiento y relación entrenador–cliente
- 🏋️ **Rutinas personalizadas** — crea y asigna planes de entrenamiento por cliente
- 🥗 **Planes de dieta** — organiza comidas y objetivos nutricionales
- 📈 **Seguimiento de progreso** — monitoriza métricas físicas y marcas personales
- 💬 **Chat interno** — comunicación directa con cada cliente desde el panel
- 💳 **Suscripciones con Stripe** — gestión de pagos mediante checkout sessions y webhooks

### Para clientes — App móvil Flutter
- 📋 **Diario de entrenamiento** — registra pesos y repeticiones en cada sesión
- 🏆 **Historial de marcas** — consulta tus récords personales por ejercicio
- 🥗 **Guía nutricional** — dieta diaria organizada por comidas
- 📊 **Progreso físico** — gráficas de evolución de peso y medidas corporales
- 💬 **Chat con tu entrenador** — comunicación directa desde la app

---

## 🏗️ Arquitectura

```
┌─────────────────────┐     ┌──────────────────────────────────────────────┐
│   Flutter App       │     │           Spring Boot API                    │
│   (Clientes)        │────▶│                                              │
└─────────────────────┘     │  ┌────────────┐   ┌──────────────────────┐  │
                            │  │Controllers │──▶│      Services        │  │
┌─────────────────────┐     │  └────────────┘   └──────────┬───────────┘  │
│   Panel Web         │     │                              │              │
│   Thymeleaf +       │────▶│  Spring Security + JWT       │              │
│   Bootstrap         │     │                   ┌──────────▼───────────┐  │
└─────────────────────┘     │                   │    Repositories      │  │
                            │                   │    JPA / Hibernate   │  │
                            │                   └──────────┬───────────┘  │
                            └──────────────────────────────┼──────────────┘
                                                           │
                                              ┌────────────▼────────────┐
                                              │   MySQL — Aiven Cloud   │
                                              └─────────────────────────┘
```

El backend sigue una **arquitectura por capas** estricta:

| Capa | Responsabilidad |
|---|---|
| **Controllers** | Manejo de peticiones HTTP, validación de entrada, respuesta REST |
| **Services** | Lógica de negocio, reglas de dominio |
| **Repositories** | Acceso a datos con Spring Data JPA |
| **Entities / DTOs** | Modelo de datos y objetos de transferencia |

---

## 🛠️ Stack tecnológico

| Área | Tecnología |
|---|---|
| Lenguaje | Java 21 |
| Framework backend | Spring Boot 3 |
| Seguridad | Spring Security + JWT |
| Persistencia | JPA / Hibernate + MySQL |
| Build | Maven |
| Panel web | Thymeleaf + Bootstrap 5 + JavaScript |
| App móvil | Flutter (Dart) |
| Pagos | Stripe (checkout sessions + webhooks) |
| Despliegue backend | Render |
| Despliegue base de datos | Aiven (MySQL Cloud) |
| Testing de API | Postman |
| Control de versiones | Git + GitHub |

---

## 🚀 Instalación

### Requisitos previos

- Java 21+
- Maven 3.8+
- MySQL 8+ (o cuenta en Aiven)
- Flutter SDK 3.x
- Cuenta de Stripe (claves de test)

### 1. Clonar el repositorio

```bash
git clone https://github.com/guillegomezlopez3/FinalTFG.git
cd FinalTFG
```

### 2. Configurar variables de entorno

Edita `src/main/resources/application.properties` o crea un archivo `.env`:

```properties
# Base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/levelup_db
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_PASSWORD

# JWT
jwt.secret=TU_CLAVE_SECRETA_JWT
jwt.expiration=86400000

# Stripe
stripe.api.key=sk_test_TU_CLAVE
stripe.webhook.secret=whsec_TU_WEBHOOK_SECRET

# SMTP (correos)
spring.mail.host=smtp.gmail.com
spring.mail.username=TU_EMAIL
spring.mail.password=TU_APP_PASSWORD
```


Panel web disponible en `http://localhost:8081`

### 4. Ejecutar la app móvil

```bash
cd mobile_app
flutter pub get
flutter run
```

> Asegúrate de que la IP en `lib/network/api_client.dart` apunta a tu servidor local (`10.0.2.2:8081` para emulador Android).

---

## 📁 Estructura del proyecto

```
FinalTFG/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/levelup/
│       │       ├── controllers/       # Endpoints REST y vistas web
│       │       ├── services/          # Lógica de negocio
│       │       ├── repositories/      # Acceso a datos (Spring Data JPA)
│       │       ├── entities/          # Entidades JPA
│       │       ├── dto/               # Data Transfer Objects
│       │       └── security/          # Configuración JWT y Spring Security
│       └── resources/
│           ├── templates/             # Vistas Thymeleaf
│           ├── static/                # CSS, JS, imágenes
│           └── application.properties
├── mobile_app/                        # Aplicación Flutter
│   └── lib/
│       ├── screens/                   # Pantallas de la app
│       ├── network/                   # Cliente HTTP y endpoints
│       └── models/                    # Modelos de datos Dart
└── DOCUMENTACION_FINAL/               # Documentación completa del TFG
```

---

## 🔌 Endpoints principales de la API

Base URL: `https://finaltfglevelup.onrender.com/api`

| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/auth/register` | Registro de usuario |
| POST | `/auth/login` | Login y obtención de JWT |
| GET | `/clients` | Listar clientes del entrenador |
| POST | `/routines` | Crear rutina de entrenamiento |
| GET | `/routines/{clientId}` | Obtener rutinas de un cliente |
| POST | `/diets` | Crear plan de dieta |
| POST | `/progress` | Registrar progreso físico |
| GET | `/progress/{clientId}` | Historial de progreso |
| POST | `/stripe/checkout` | Crear sesión de pago |
| POST | `/stripe/webhook` | Receptor de eventos Stripe |

---

## 👨‍💻 Autor

**Guillermo Gómez López**  
Trabajo Fin de Grado — DAM (Desarrollo de Aplicaciones Multiplataforma)  
FEMPA, Alicante · 2025

📧 gomezlopezguille@gmail.com  
💼 [linkedin.com/in/guillermo-gomez-lopez-84770b179](https://www.linkedin.com/in/guillermo-gomez-lopez-84770b179/)

---

*Proyecto académico desarrollado como TFG del ciclo superior DAM — Alicante, 2025*
