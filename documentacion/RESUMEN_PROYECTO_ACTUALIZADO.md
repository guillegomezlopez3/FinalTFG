# Resumen del Proyecto y PRÓXIMOS PASOS (TFGFitApp)

## 📌 Estado Actual del Proyecto

En este punto hemos construido una base técnica sólida y robusta en Spring Boot con un front-end en Thymeleaf/Bootstrap. Las piezas clave que ya están implementadas y operativas son:

### 1. Modelo de Datos y Entidades Relacionales (JPA)
- **Cuentas y Seguridad**: Entidad `User` mapeada con credenciales cifradas (BCrypt) y roles jerárquicos (ADMIN, TRAINER, CLIENT).
- **Perfiles Centrales**: `Trainer` (entrenadores) y `Client` (clientes ligados a un entrenador).
- **Lógica Deportiva y Nutricional**: `Diet` (con `DietMeal`), `WorkoutPlan` (con `WorkoutDay` y `Exercise`), `ProgressRecord` para las métricas evolutivas.

### 2. Autenticación y Autorización (Spring Security + JWT)
- Configuración moderna sobre Spring Security 6.
- Endpoints públicos como `/auth/login` y `/auth/register`. 
- Uso de **JWT Filters** transparentes para la capa de la API interna.

### 3. API y Servicios (Backend)
- Servicios desacoplados de los repositorios y protegidos. Funcionalidad clara mediante `DTOs` limpios para no exponer detalles de los objetos de persistencia.
- Manejo de excepciones global ( `@ControllerAdvice` en `GlobalExceptionHandler`) entregando respuestas HTTP claras frente a fallos.

### 4. UI/UX (Frontend en Thymeleaf)
- Plantillas dinámicas, limpias y agradables con un diseño simple/minimalista.
- Sistema de fragmentos (`sidebar`, `head`) para no repetir código (DRY).
- Paneles privados (`dashboard/admin`, `dashboard/clients`, `dashboard/profile`, `dashboard/progress`, etc.).
- Comportamientos reactivos simples en JS asíncrono haciendo uso de la API REST que creamos.

---

## 🚀 Lista de Tareas (Roadmap para una App de 10)

Para llevar esta plataforma a un nivel "Premium", aquí tienes las mejores iniciativas a implementar (ordenadas por prioridad y visibilidad):

### A. Mejoras de UI/UX (Experiencia de Usuario)
- [ ] **Modo Oscuro (Dark Mode)**: Agregar un _toggle_ (interruptor) global integrado con el sistema de temas (Bootstrap o CSS puro), guardado en `localStorage`.
- [ ] **Gráficos Estadísticos (Chart.js / Recharts)**:
  - En el panel del Cliente (Mi Progreso), mostrar una gráfica lineal con la evolución de su peso y porcentaje de grasa corporal.
  - En el panel Admin o Trainer mostrar gráficas de evolución de membresías o adopción.
- [ ] **Drag & Drop (Arrastrar y soltar)**: En el panel de dietas o entremos del entrenador, permitir ordenar comidas o ejercicios con el ratón interactivo.

### B. Nuevas Funcionalidades "Core"
- [ ] **Generación de PDFs**: Permitir a los clientes y entrenadores descargar Planes de Entrenamiento y Dietas en un archivo PDF elegante y formal.
- [ ] **Sistema de Notificaciones (WebSockets / SSE)**: Avisar al usuario en tiempo real cuando su entrenador crea una nueva dieta o cuando finaliza su programa, o un chat directo Entrenador-Cliente.
- [ ] **Subida de Archivos Multimedia (S3 o LocalStorage)**:
  - Permitir a los usuarios y entrenadores subir su **foto de perfil** (avatar real) y almacenarlo como Multipart.
  - Galería de progreso: permitir a los clientes subir fotos de su avance físico asociadas al progreso.

### C. Refinamientos de Seguridad y Autenticación
- [ ] **Tokens de Refresco (Refresh Tokens)**: Que la sesión no desloguee bruscamente, sino que el cliente renueve su JWT.
- [ ] **Recuperación de Contraseñas**: El clásico "¿Olvidaste tu contraseña?". Flujo con envío de email (Spring Mail) que incluye un token de reseteo temporal.

### D. Mejoras de Ingeniería y Mantenibilidad
- [ ] **Paginación avanzada y Filtros**: Si un entrenador llega a tener cientos de clientes, es necesario paginación en el servidor y buscadores/filtros dinámicos en el cliente.
- [ ] **Testing Exhaustivo (QA)**: Validar todo el frontend usando tests E2E con _Cypress_ o _Playwright_, y aumentar el coverage al 85%+ mínimo usando _JUnit5_ con _Mockito_.
- [ ] **Dockerización total del entorno**: Asegurarnos de que el `compose.yaml` ya no sea sólo para MySQL, sino para desplegar la app entera junto a otros servicios (ej: un Redis o RabbitMQ si lo necesitamos).

---
> Recomendación actual: Todos los cambios principales que hemos llevado a cabo hasta ahora han sido confirmados en Git. Ya tenemos un historial limpio al cual volver en caso de problemas.

