# 🔧 Guía de Corrección - Base de Datos ENUM

## ❌ Problema

La base de datos tiene los valores ENUM en minúsculas (`admin`, `trainer`, `client`) pero Java espera valores en mayúsculas (`ADMIN`, `TRAINER`, `CLIENT`).

## ✅ Solución

### Paso 1: Ejecutar el Script SQL

**Opción A - MySQL Workbench (RECOMENDADO):**

1. Abre **MySQL Workbench**
2. Conéctate a tu servidor local
3. Abre el archivo `fix_enum_case.sql`
4. Click en el botón **Execute** (rayo) o presiona `Ctrl+Shift+Enter`
5. Verifica que se ejecute sin errores

**Opción B - Línea de Comandos:**

```bash
# Navega al directorio del proyecto
cd "C:\Users\USUARIO\OneDrive - fempa.es\Escritorio\AccesoAdatosPracticaFinal\TFGFitApp"

# Ejecuta el script
mysql -u root -p personal_trainer_manager < fix_enum_case.sql
# Cuando pida password: Pipas1372.
```

### Paso 2: Verificar los Cambios

Ejecuta esta consulta en MySQL:

```sql
USE personal_trainer_manager;
SELECT id, name, email, role FROM users;
```

Deberías ver que todos los roles están en MAYÚSCULAS: `ADMIN`, `TRAINER`, `CLIENT`

### Paso 3: Reiniciar la Aplicación

Una vez ejecutado el script, reinicia la aplicación Spring Boot.

## 📋 ¿Qué hace el script?

1. **Modifica temporalmente el ENUM** para aceptar tanto mayúsculas como minúsculas
2. **Actualiza todos los registros** existentes de minúsculas a mayúsculas
3. **Elimina los valores en minúsculas** del ENUM
4. **Repite el proceso** para las tablas:
   - `users` (columna `role`)
   - `clients` (columna `level`)
   - `workout_days` (columna `day_of_week`)

## ⚠️ IMPORTANTE

**Ejecuta este script ANTES de arrancar la aplicación** de nuevo. Si no lo haces, seguirás teniendo el error:

```
No enum constant com.tfgfitapp.tfgfitapp.enumeration.Role.trainer
```

## 🎯 Resultado Esperado

✅ Roles en la base de datos: `ADMIN`, `TRAINER`, `CLIENT`
✅ Niveles en la base de datos: `BEGINNER`, `INTERMEDIATE`, `ADVANCED`
✅ Días de la semana: `MONDAY`, `TUESDAY`, etc.
✅ Login y registro funcionando correctamente

---

**Ubicación del script:** `fix_enum_case.sql` en la raíz del proyecto

