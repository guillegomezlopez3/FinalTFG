# 🚀 Guía rápida de arranque — TFGFitApp

## ⚠️ IMPORTANTE: Problema de instancias múltiples

Si ves el error **"Port 8081 was already in use"**, es porque tienes **instancias previas de Java corriendo**.

### Solución rápida (ejecuta ANTES de arrancar):

```powershell
# Opción 1: Script automático (RECOMENDADO)
.\cleanup-java.ps1

# Opción 2: Manual
Stop-Process -Name java -Force
```

---

## ✅ Puerto 8081 configurado

La aplicación está configurada para arrancar en el **puerto 8081**.

---

## Opción 1: Arrancar desde IntelliJ IDEA (RECOMENDADO)

Veo que ya intentaste arrancar desde IntelliJ. Ahora que el puerto está libre:

1. **Abre IntelliJ IDEA**
2. Busca la clase `TfgFitAppApplication.java`
3. Haz clic derecho → **Run 'TfgFitAppApplication'**
4. Espera a ver en la consola:
   ```
   Started TfgFitAppApplication in X.XXX seconds
   ```

---

## Opción 2: Arrancar desde Maven (terminal)

```powershell
cd "C:\Users\USUARIO\OneDrive - fempa.es\Escritorio\AccesoAdatosPracticaFinal\TFGFitApp"
.\mvnw.cmd spring-boot:run
```

**Espera 30-40 segundos** hasta que veas:
```
Started TfgFitAppApplication in X.XXX seconds
```

---

## Verificación de arranque exitoso

### 1. Comprobar que el puerto está en uso:
```powershell
netstat -ano | Select-String ":8081"
```
Debes ver:
```
TCP    0.0.0.0:8081    0.0.0.0:0    LISTENING    [PID]
```

### 2. Probar la landing page:
Abre el navegador en: **http://localhost:8081/**

Deberías ver la landing page con el hero naranja y el logo "TFGFitApp"

---

## URLs de la aplicación

| Página | URL | Descripción |
|--------|-----|-------------|
| 🏠 **Landing** | http://localhost:8081/ | Página principal pública |
| 🔐 **Login** | http://localhost:8081/login | Iniciar sesión |
| 📝 **Registro** | http://localhost:8081/register | Crear cuenta (TRAINER / CLIENT) |
| 📊 **Dashboard** | http://localhost:8081/dashboard | Panel privado (requiere login) |
| 👥 **Clientes** | http://localhost:8081/dashboard/clients | Gestión de clientes (TRAINER/ADMIN) |
| 🍽️ **Dietas** | http://localhost:8081/dashboard/diets | Planes de nutrición |
| 💪 **Entrenamientos** | http://localhost:8081/dashboard/workouts | Rutinas de entrenamiento |
| 📈 **Progreso** | http://localhost:8081/dashboard/progress | Registros de evolución (CLIENT) |
| 🛡️ **Admin** | http://localhost:8081/dashboard/admin | Panel de administración |
| 👤 **Perfil** | http://localhost:8081/dashboard/profile | Mi perfil |

---

## Credenciales por defecto

### Usuario ADMIN (creado automáticamente)
- **Email:** `admin@tfgfitapp.com`
- **Password:** `Admin1234!`

### Para crear otros usuarios:
1. Ve a http://localhost:8081/register
2. Selecciona **TRAINER** o **CLIENT**
3. Completa el formulario y registra

---

## Logs y depuración

### Ver logs en IntelliJ IDEA:
- Panel inferior → pestaña **Run**
- Busca líneas con `ERROR` o `WARN`

### Ver logs desde Maven:
Los logs aparecen directamente en la consola donde ejecutaste `mvnw.cmd`

### Si hay errores 500:
1. Verifica que **MySQL esté corriendo** en el puerto 3306
2. Comprueba que la base de datos **`personal_trainer_manager`** exista
3. Revisa las credenciales en `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/personal_trainer_manager?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   spring.datasource.username=root
   spring.datasource.password=Pipas1372.
   ```

---

## Solución de problemas comunes

### Puerto 8081 ocupado
```powershell
# Ver qué proceso lo usa
netstat -ano | Select-String ":8081"

# Matar el proceso (cambiar PID por el que salga)
Stop-Process -Id [PID] -Force
```

### MySQL no conecta
```powershell
# Verificar si MySQL está corriendo
Get-Service -Name MySQL* | Select-Object Status, Name
```

### Cambiar puerto (si 8081 está ocupado)
Edita `application.properties`:
```properties
server.port=8082
```

---

## 🎉 Todo listo

Ahora puedes arrancar la aplicación con cualquiera de las dos opciones y acceder a todas las funcionalidades implementadas.

Si tienes problemas, revisa:
1. **MySQL** está corriendo
2. La base de datos existe
3. No hay otro proceso en el puerto 8081
4. Las credenciales de `application.properties` son correctas

