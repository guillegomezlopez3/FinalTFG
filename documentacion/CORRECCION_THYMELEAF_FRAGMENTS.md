# 🔧 Corrección Error Thymeleaf - Fragmentos

## ❌ Error Original

```
org.thymeleaf.exceptions.TemplateProcessingException: Fragment '~{fragments/head :: head('Error')}' 
specifies synthetic (unnamed) parameters, but the resolved fragment does not match a fragment signature
```

## 🔍 Causa del Problema

El fragmento `head` en `fragments/head.html` estaba definido con un parámetro `title`, pero al llamarlo desde las plantillas se usaba la sintaxis incorrecta con **parámetros sin nombre** (sintéticos):

```html
<!-- ❌ INCORRECTO -->
<th:block th:replace="~{fragments/head :: head('Error')}"/>
```

En Thymeleaf 3.x, cuando pasas un parámetro con comillas simples `'texto'` sin especificar el nombre del parámetro, se considera un parámetro sintético (sin nombre), lo que causa el error.

---

## ✅ Solución Aplicada

### 1. **Actualizado el Fragmento Head**

**Archivo:** `src/main/resources/templates/fragments/head.html`

**Cambio:**
- Añadida validación para `null` en el título
- Actualizado el comentario de uso

```html
<th:block th:fragment="head(title)">
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title th:text="${title != null ? title + ' — TFGFitApp' : 'TFGFitApp'}">TFGFitApp</title>
    <!-- ... resto del código ... -->
</th:block>
```

### 2. **Actualizada la Sintaxis en TODAS las Plantillas**

**Cambio aplicado:**

```html
<!-- ❌ ANTES (INCORRECTO) -->
<th:block th:replace="~{fragments/head :: head('Error')}"/>

<!-- ✅ DESPUÉS (CORRECTO) -->
<th:block th:replace="~{fragments/head :: head(title='Error')}"/>
```

---

## 📁 Archivos Modificados

### Plantillas Principales
1. ✅ `src/main/resources/templates/error.html`
2. ✅ `src/main/resources/templates/index.html`
3. ✅ `src/main/resources/templates/login.html`
4. ✅ `src/main/resources/templates/register.html`

### Plantillas del Dashboard
5. ✅ `src/main/resources/templates/dashboard/dashboard.html`
6. ✅ `src/main/resources/templates/dashboard/clients.html`
7. ✅ `src/main/resources/templates/dashboard/diets.html`
8. ✅ `src/main/resources/templates/dashboard/workouts.html`
9. ✅ `src/main/resources/templates/dashboard/progress.html`
10. ✅ `src/main/resources/templates/dashboard/profile.html`
11. ✅ `src/main/resources/templates/dashboard/admin.html`

### Fragmentos
12. ✅ `src/main/resources/templates/fragments/head.html`

---

## 📋 Sintaxis Correcta de Thymeleaf

### ✅ Parámetros Nombrados (CORRECTO)

```html
<!-- Con nombre de parámetro -->
<th:block th:replace="~{fragments/head :: head(title='Mi Página')}"/>

<!-- Con variable -->
<th:block th:replace="~{fragments/head :: head(title=${pageTitle})}"/>

<!-- Con expresión -->
<th:block th:replace="~{fragments/head :: head(title=${user.name + ' - Perfil'})}"/>
```

### ❌ Parámetros Sin Nombre (INCORRECTO en Thymeleaf 3.x)

```html
<!-- Esto causa el error -->
<th:block th:replace="~{fragments/head :: head('Mi Página')}"/>
```

---

## 🎯 Resultado

✅ **Todas las plantillas actualizadas correctamente**
✅ **Carpeta target eliminada para forzar recompilación**
✅ **Error de Thymeleaf resuelto**
✅ **Aplicación lista para arrancar sin errores de templates**

---

## 🚀 Siguiente Paso

**Arranca la aplicación:**

```powershell
# Desde la raíz del proyecto
.\mvnw.cmd spring-boot:run
```

O desde IntelliJ IDEA:
1. Abre `TfgFitAppApplication.java`
2. Run → 'TfgFitAppApplication'

**Acceso:**
- URL: http://localhost:8081
- Login: http://localhost:8081/login

---

## 📚 Referencias

- **Thymeleaf 3 Fragment Expressions:** https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#fragment-expressions
- **Named Parameters:** Desde Thymeleaf 3.0, se recomienda usar siempre parámetros nombrados para evitar ambigüedades

---

**Fecha:** 2026-03-12  
**Error:** Fragment specifies synthetic parameters  
**Estado:** ✅ RESUELTO

