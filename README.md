# LevelUp - Sistema Integral de Gestión Fitness

![LevelUp Banner](https://img.shields.io/badge/Status-Project_Completed-brightgreen?style=for-the-badge)
![Tech Stack](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![Framework](https://img.shields.io/badge/Spring_Boot-3.2-6DB33F?style=for-the-badge&logo=spring-boot)
![Mobile](https://img.shields.io/badge/Flutter-3.x-02569B?style=for-the-badge&logo=flutter)

**LevelUp** es una plataforma multiplataforma diseñada para modernizar el entrenamiento personal. Olvida los Excels y PDFs estáticos; LevelUp ofrece una experiencia interactiva tanto para entrenadores como para clientes.

## 🌟 Características Principales

### Para Entrenadores (Panel Web)
*   **Gestión de Suscripciones:** Pasarela de pago integrada con **Stripe**.
*   **Planificador Dinámico:** Crea rutinas de entrenamiento y dietas personalizadas.
*   **Seguimiento en Tiempo Real:** Monitoriza el progreso de tus clientes y sus marcas.
*   **Chat Centralizado:** Comunicación directa sin salir de la plataforma.

### Para Clientes (App Móvil)
*   **Diario de Entrenamiento:** Registro interactivo de pesos y repeticiones.
*   **Historial de Marcas:** Consulta tus récords personales al instante durante el ejercicio.
*   **Guía Nutricional:** Tu dieta diaria organizada por comidas.
*   **Progreso Físico:** Gráficas de evolución de peso y medidas corporales.

## 🛠️ Requisitos del Sistema

*   **Java 21 JDK**
*   **Maven 3.x**
*   **MySQL 8.x**
*   **Flutter SDK 3.x** (para la app móvil)

## 🚀 Instalación y Configuración

### 1. Clonar el repositorio
```bash
git clone https://github.com/tu-usuario/levelup.git
cd levelup
```

### 2. Configuración del Backend
Edita el archivo `src/main/resources/application.properties` y configura tus credenciales:
*   Base de Datos (MySQL)
*   Servidor SMTP (para correos)
*   Claves de Stripe

### 3. Ejecutar el Servidor
```bash
./mvnw spring-boot:run
```
El panel web estará disponible en `http://localhost:8081`.

### 4. Configurar la App Móvil
1. Entra en el directorio `mobile_app`.
2. Asegúrate de que la IP en `lib/network/api_client.dart` apunte a tu servidor.
3. Ejecuta:
```bash
flutter pub get
flutter run
```

## 📄 Documentación Técnica
El proyecto cuenta con documentación técnica autogenerada y manual:
*   **Javadoc:** `target/reports/apidocs/`
*   **DartDoc:** `mobile_app/doc/api/`
*   **Introducción y Diseño:** Consulta la carpeta `DOCUMENTACION_FINAL` para una explicación detallada del proyecto.

---
*Desarrollado como Proyecto de Fin de Grado (TFG).*
*© 2026 LevelUp Team*
