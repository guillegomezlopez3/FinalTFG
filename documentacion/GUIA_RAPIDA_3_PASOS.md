# ��� GUÍA RÁPIDA - Corrección en 3 Pasos

## 🎯 Problema

Al intentar hacer login o registro aparece:
```
Error interno del servidor
No enum constant com.tfgfitapp.tfgfitapp.enumeration.Role.trainer
```

## ✅ Solución Rápida (3 minutos)

### PASO 1: Detener la Aplicación

Si la aplicación está corriendo, deténla:

**En IntelliJ:**
- Click en el botón rojo STOP en la barra de herramientas

**En Terminal:**
- Presiona `Ctrl + C`

**Forzar cierre si es necesario:**
```powershell
Get-Process java -ErrorAction SilentlyContinue | Stop-Process -Force
```

---

### PASO 2: Ejecutar el Script SQL

El problema está en la base de datos, no en el código.

**📝 Opción A - MySQL Workbench (RECOMENDADO):**

1. **Abre MySQL Workbench**

2. **Conéctate a tu servidor:**
   - Host: `localhost`
   - Port: `3306`
   - User: `root`
   - Password: `Pipas1372.`

3. **Abre el script:**
   - File → Open SQL Script...
   - Navega a: `C:\Users\USUARIO\OneDrive - fempa.es\Escritorio\AccesoAdatosPracticaFinal\TFGFitApp\reset_database.sql`

4. **Ejecuta el script:**
   - Click en el botón del rayo ⚡ (Execute)
   - O presiona `Ctrl + Shift + Enter`

5. **Verifica el resultado:**
   Deberías ver:
   ```
   ✅ Base de datos lista para arrancar la aplicación
   ```

**📝 Opción B - Línea de Comandos:**

```powershell
# 1. Navega al directorio del proyecto
cd "C:\Users\USUARIO\OneDrive - fempa.es\Escritorio\AccesoAdatosPracticaFinal\TFGFitApp"

# 2. Ejecuta el script (cuando pida password, escribe: Pipas1372.)
mysql -u root -p personal_trainer_manager < reset_database.sql
```

---

### PASO 3: Arrancar la Aplicación

**Opción A - IntelliJ IDEA:**
1. Abre `TfgFitAppApplication.java`
2. Click derecho → Run 'TfgFitAppApplication'
3. Espera a ver: `Started TfgFitAppApplication in X.XXX seconds`

**Opción B - Terminal:**
```powershell
.\mvnw.cmd spring-boot:run
```

**Logs Esperados:**

Deberías ver algo como:
```
INFO - HikariPool-1 - Start completed
INFO - 🔑 Usuario ADMIN creado con email: admin@tfgfitapp.com
INFO - Started TfgFitAppApplication in 8.547 seconds
```

---

## 🎉 LISTO - Probar la Aplicación

### 1. Abrir el Navegador

```
http://localhost:8081/login
```

### 2. Hacer Login como ADMIN

```
Email: admin@tfgfitapp.com
Password: Admin1234!
```

Si entras correctamente, **¡PROBLEMA RESUELTO!** ✅

### 3. Probar Registro

1. Click en "Registrate aquí"
2. Rellena el formulario
3. Selecciona: **Entrenador** o **Cliente**
4. Click en "Crear cuenta"

Si el registro funciona, **¡TODO PERFECTO!** ✅

---

## ❓ Si Algo Sale Mal

### Error: "Port 8081 already in use"

```powershell
# Matar todos los Java
Get-Process java | Stop-Process -Force

# Esperar 2 segundos
Start-Sleep -Seconds 2

# Arrancar de nuevo
.\mvnw.cmd spring-boot:run
```

### Error: "Cannot connect to MySQL"

1. Verifica que MySQL esté corriendo
2. Abre MySQL Workbench y prueba conectarte
3. Verifica password: `Pipas1372.`

### Sigue sin funcionar el login

Ejecuta esto en MySQL Workbench:

```sql
USE personal_trainer_manager;
SELECT id, name, email, role FROM users;
```

**Verifica que:**
- El role debe estar en MAYÚSCULAS: `ADMIN`, `TRAINER`, `CLIENT`
- Si está en minúsculas (`admin`, `trainer`), el script no se ejecutó correctamente

---

## 📋 Resumen

✅ **Qué hace el script:**
1. Borra todos los datos existentes
2. Actualiza los ENUMs a MAYÚSCULAS
3. Deja la BD lista para que Spring Boot cree el admin

✅ **Resultado esperado:**
- Login funciona ✅
- Registro funciona ✅
- Todas las funcionalidades operativas ✅

✅ **Credenciales por defecto:**
```
ADMIN:
- Email: admin@tfgfitapp.com
- Password: Admin1234!

Nuevos usuarios:
- Se crean con el formulario de registro
- Password: la que elijas (mínimo 6 caracteres)
```

---

## 🎯 Siguiente Paso Después de Corregir

Una vez que login y registro funcionen:

1. **Como ADMIN:** Puedes ver estadísticas y gestionar el sistema
2. **Registrar un TRAINER:** Crea una cuenta de entrenador
3. **Registrar un CLIENT:** Crea una cuenta de cliente
4. **Como TRAINER:** Gestiona clientes, dietas y planes
5. **Como CLIENT:** Ve tus dietas y planes asignados

---

**Tiempo Total:** ~3 minutos
**Dificultad:** Fácil
**Resultado:** Aplicación 100% funcional ✅

