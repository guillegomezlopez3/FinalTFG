# 🔄 Flujo de Inicialización - TFGFitApp

## 🎯 Pregunta Común

**"¿Tengo que ejecutar el script SQL cada vez que arranque la aplicación?"**

**Respuesta: NO, SOLO UNA VEZ**

---

## 📋 Situaciones y Soluciones

### 🆕 **SITUACIÓN 1: Primera Vez (AHORA)**

Tu base de datos ya existe pero tiene los ENUMs en minúsculas.

**Solución: Ejecutar `reset_database.sql` UNA SOLA VEZ**

```bash
# Solo esta vez
mysql -u root -p personal_trainer_manager < reset_database.sql
```

Después de esto:
- ✅ ENUMs corregidos para siempre
- ✅ Base de datos lista
- ✅ Ya no necesitas ejecutar scripts SQL

---

### ♻️ **SITUACIÓN 2: Día a Día (Uso Normal)**

Ya ejecutaste el script de corrección anteriormente.

**Solución: Solo arrancar la aplicación**

```bash
# Todos los días, simplemente:
.\mvnw.cmd spring-boot:run
```

O desde IntelliJ: Run → 'TfgFitAppApplication'

**NO necesitas:**
- ❌ Ejecutar scripts SQL
- ❌ Tocar la base de datos
- ❌ Hacer nada especial

---

### 🔄 **SITUACIÓN 3: Recrear la BD desde Cero**

Si en el futuro necesitas borrar y recrear toda la base de datos.

**Solución: Usar el script CORRECTO**

```bash
# Usa el nuevo script con ENUMs corregidos
mysql -u root -p < create_database_CORRECTED.sql
```

Este script:
- ✅ Crea la BD con ENUMs en MAYÚSCULAS desde el principio
- ✅ No necesitas corrección posterior
- ✅ Compatible con Java

---

## 🎯 Flujo Recomendado

### **AHORA (Solo esta vez):**

```
1. Detener aplicación
2. Ejecutar reset_database.sql (UNA VEZ)
3. Arrancar aplicación
4. Login con admin / Admin1234!
5. ✅ LISTO
```

### **MAÑANA y SIEMPRE:**

```
1. Arrancar aplicación
2. Usar normalmente
3. ✅ LISTO
```

---

## 📁 Scripts Disponibles

### **`reset_database.sql`** ⚠️ SOLO UNA VEZ
- Borra datos existentes
- Corrige ENUMs a mayúsculas
- Usar AHORA para corregir el problema

### **`fix_enum_case.sql`** 💾 Si quieres conservar datos
- Actualiza ENUMs sin borrar datos
- Migra valores existentes
- Alternativa a reset_database.sql

### **`create_database_CORRECTED.sql`** 🆕 Para el futuro
- Crea BD desde cero CON los ENUMs correctos
- Usar si necesitas recrear la BD en el futuro
- Ya no necesitarás correcciones

---

## ❓ Preguntas Frecuentes

### **¿Por qué pasó esto?**
El script SQL original tenía:
```sql
role ENUM('admin', 'trainer', 'client')  -- Minúsculas
```

Pero Java espera:
```java
public enum Role { ADMIN, TRAINER, CLIENT }  // Mayúsculas
```

### **¿Se volverá a romper?**
NO. Una vez corregido, funciona para siempre.

### **¿Qué pasa con los datos que cree después?**
Se guardan correctamente. El problema solo afecta a:
- Usuarios creados ANTES de la corrección
- Con el script SQL viejo

Después de la corrección:
- ✅ Nuevos usuarios se guardan bien
- ✅ Login funciona correctamente
- ✅ Registro funciona correctamente

### **¿Puedo evitar perder datos?**
Sí, usa `fix_enum_case.sql` en lugar de `reset_database.sql`.

Ese script:
- ✅ Mantiene todos los datos existentes
- ✅ Solo actualiza los ENUMs
- ✅ Migra valores de minúsculas a mayúsculas

---

## 🎯 Resumen

| Situación | Qué Hacer | Frecuencia |
|-----------|-----------|------------|
| **Primera vez (AHORA)** | Ejecutar `reset_database.sql` | UNA SOLA VEZ |
| **Uso diario** | Arrancar la aplicación | SIEMPRE |
| **Recrear BD (futuro)** | Usar `create_database_CORRECTED.sql` | Si es necesario |

---

## ✅ Después de la Corrección

Una vez ejecutado el script, tu rutina será:

**1. Abrir terminal o IntelliJ**
**2. Arrancar aplicación**
**3. Usar la aplicación**

¡Así de simple! 🚀

---

**Fecha:** 2026-03-12  
**Conclusión:** El script SQL es solo un **"parche de una vez"** para corregir un problema inicial. Después de eso, la aplicación funciona normalmente sin necesidad de scripts.

