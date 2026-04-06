# Correcciones críticas aplicadas — TFGFitApp

## 🔴 Bugs críticos resueltos

### 1. SecurityConfig — Rutas web bloqueadas
**Problema:** Todas las rutas `/dashboard/**` devolvían 403 porque Spring Security exigía JWT, pero los navegadores no envían el `localStorage` automáticamente en las navegaciones.

**Solución:** Se añadió `.requestMatchers("/dashboard", "/dashboard/**").permitAll()` en `SecurityConfig.java`. La autenticación real se hace client-side con JS + JWT en cada llamada AJAX a `/api/**`.

```java
// Antes: .anyRequest().authenticated()  ❌ bloqueaba /dashboard
// Ahora: /dashboard/** permitido, seguridad en /api/** ✅
```

---

### 2. workouts.html — Modal roto con `location.reload()`
**Problema:** Al añadir/eliminar ejercicios se llamaba a `location.reload()`, lo que recargaba toda la página y cerraba el modal.

**Solución:** 
- Se creó la variable global `currentPlanId` para mantener el contexto del plan abierto
- Se separó la lógica de carga en `loadDaysContent(planId)` 
- `addExercise()` y `deleteExercise()` ahora recargan solo el contenido del modal

```javascript
// Antes: location.reload() ❌
// Ahora: await loadDaysContent(currentPlanId) ✅
```

---

### 3. dashboard.html — CLIENT con `clientId = 0`
**Problema:** El dashboard del rol CLIENT llamaba a `/diets/client/0` porque `clientId` no estaba en la caché del usuario al cargar.

**Solución:** 
- Se llama primero a `/api/me` para obtener el `clientId` real
- Se guarda en localStorage con `saveUser({ ...getUser(), clientId })`
- Solo después se hacen las llamadas a dietas y planes

```javascript
// Ahora: primero carga /me, guarda clientId, luego consulta con ID real
```

---

### 4. profile.html — Llamada a `/clients/null`
**Problema:** Si un CLIENT no tenía `clientId` asignado, se hacía `fetch('/clients/null')` → 404.

**Solución:**
- Se valida `if (!data.clientId)` antes de hacer la llamada
- Se muestra mensaje informativo si no tiene entrenador asignado

```javascript
if (!data.clientId) {
    // Mostrar mensaje, no hacer fetch
    return;
}
```

---

## 🟢 Mejoras añadidas

### 5. Página de error personalizada
Se creó `templates/error.html` con diseño consistente para errores 404/500 en lugar de la página blanca de Spring Boot.

### 6. Favicon deportivo
Se creó `static/images/favicon.svg` con el icono de pulso naranja y se añadió al `head.html`.

---

## Estado final del proyecto

✅ **Backend completo:** 
- REST API con JWT + roles (ADMIN, TRAINER, CLIENT)
- 7 entidades JPA + repositorios + servicios
- Control de acceso granular con `@PreAuthorize`
- Exportación CSV de progreso
- 26 tests unitarios (Mockito)
- 29 tests de integración (@SpringBootTest)

✅ **Frontend completo:**
- 10 templates Thymeleaf con diseño deportivo consistente
- Paleta naranja (#FF5722) con sidebar oscuro (#0f1623)
- Autenticación client-side (JWT en localStorage)
- CRUD completo de clientes, dietas, planes y progreso
- Filtros, paginación, modales y exportación CSV

✅ **Sin bugs críticos conocidos**

---

## Próximos pasos opcionales (fuera de alcance TFG básico)

- [ ] Validación avanzada de formularios con mensajes inline
- [ ] Gráficos de evolución del progreso (Chart.js)
- [ ] Sistema de notificaciones push
- [ ] Exportación PDF con iText/OpenPDF
- [ ] Tests E2E con Selenium/Playwright
- [ ] Despliegue en Docker + Docker Compose

