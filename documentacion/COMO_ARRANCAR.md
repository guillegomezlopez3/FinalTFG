# 🚀 Guía de Arranque - TFGFitApp

## ✅ Puerto 8081 Liberado

El puerto 8081 está ahora **completamente libre** y listo para usar.

---

## 🎯 Cómo Arrancar la Aplicación

### **Opción 1: Desde IntelliJ IDEA (RECOMENDADO)**

1. **Abre IntelliJ IDEA**
2. **Navega a la clase principal:**
   ```
   src/main/java/com/tfgfitapp/tfgfitapp/TfgFitAppApplication.java
   ```
3. **Click derecho** en el archivo
4. **Selecciona:** `Run 'TfgFitAppApplication'`
5. **Espera** a que aparezca en la consola:
   ```
   Started TfgFitAppApplication in X.XXX seconds
   ```

### **Opción 2: Desde Terminal (PowerShell)**

```powershell
cd "C:\Users\USUARIO\OneDrive - fempa.es\Escritorio\AccesoAdatosPracticaFinal\TFGFitApp"
.\mvnw.cmd spring-boot:run
```

⚠️ **IMPORTANTE:** Si usas esta opción, la terminal se quedará bloqueada mostrando logs. Para detener la aplicación, presiona `Ctrl + C`.

---

## 🌐 Acceso a la Aplicación

Una vez que la aplicación arranque exitosamente:

```
URL Principal: http://localhost:8081
Página de Login: http://localhost:8081/login
```

### 🔐 Credenciales de Acceso

**Administrador:**
```
Email: admin@tfgfitapp.com
Password: Admin1234!
```

---

## 🔧 Si el Puerto Sigue Ocupado

Si al arrancar te dice que el puerto está en uso, ejecuta este comando para liberar todos los procesos Java:

```powershell
Get-Process java -ErrorAction SilentlyContinue | Stop-Process -Force
```

Luego espera 2-3 segundos y vuelve a arrancar.

---

## 📊 Verificar Estado

### Ver si hay procesos Java corriendo:
```powershell
Get-Process java -ErrorAction SilentlyContinue
```

### Ver si el puerto 8081 está en uso:
```powershell
netstat -ano | Select-String ":8081"
```

Si no muestra nada = puerto libre ✅

---

## 🆘 Solución de Problemas

### Problema: "Port 8081 already in use"
**Solución:**
```powershell
# Matar todos los Java
Get-Process java -ErrorAction SilentlyContinue | Stop-Process -Force

# Esperar 2 segundos
Start-Sleep -Seconds 2

# Arrancar de nuevo
```

### Problema: "Cannot connect to database"
**Verificar:**
1. MySQL está corriendo
2. La base de datos `personal_trainer_manager` existe
3. Usuario: `root` / Password: `Pipas1372.`

### Problema: Cambios no se reflejan
**Solución:**
1. Detener la aplicación
2. Ejecutar:
   ```powershell
   .\mvnw.cmd clean compile
   ```
3. Arrancar de nuevo

---

## 📝 Logs de Arranque Esperados

Cuando la aplicación arranca correctamente, deberías ver algo como:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.4.5)

INFO - Starting TfgFitAppApplication...
INFO - No active profile set, falling back to 1 default profile: "default"
INFO - Bootstrapping Spring Data JPA repositories...
INFO - Finished Spring Data repository scanning...
INFO - Tomcat initialized with port 8081 (http)
INFO - Starting service [Tomcat]
INFO - HHH000204: Processing PersistenceUnitInfo [name: default]
INFO - HikariPool-1 - Starting...
INFO - HikariPool-1 - Start completed.
INFO - ✅ Usuario ADMIN ya existe: admin@tfgfitapp.com
INFO - Started TfgFitAppApplication in 8.547 seconds
```

---

## ✨ Estado Actual

```
✅ Puerto 8081: LIBRE
✅ Compilación: EXITOSA
✅ Base de datos: Configurada
✅ Aplicación: Lista para arrancar
```

---

**Última actualización:** 2026-03-12  
**Estado:** ✅ LISTO PARA USAR

