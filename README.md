# TFGFitApp 🏋️

Aplicación web para gestión de entrenamiento personal. Sistema completo con roles (Admin, Entrenador, Cliente), autenticación JWT, gestión de dietas, planes de entrenamiento y seguimiento de progreso.

---

## 🚀 Arranque rápido

### 1. **ANTES DE ARRANCAR** (IMPORTANTE)
```powershell
# Limpia instancias previas de Java
.\cleanup-java.ps1
```

### 2. Arrancar la aplicación

**Opción A: IntelliJ IDEA** (recomendado)
- Abre `TfgFitAppApplication.java`
- Click en Run (▶️)
- Espera mensaje: `Started TfgFitAppApplication`

**Opción B: Maven**
```powershell
.\mvnw.cmd spring-boot:run
```

### 3. Acceder a la aplicación
📍 **http://localhost:8081/**

---

## 🔑 Credenciales por defecto

**Usuario ADMIN:**
- Email: `admin@tfgfitapp.com`
- Password: `Admin1234!`

---

## 📚 Documentación

| Documento | Descripción |
|-----------|-------------|
| [GUIA_ARRANQUE.md](documentacion/GUIA_ARRANQUE.md) | Guía completa de arranque y URLs |
| [PROBLEMA_MULTIPLES_INSTANCIAS.md](documentacion/PROBLEMA_MULTIPLES_INSTANCIAS.md) | Solución al error "Port already in use" |
| [CORRECCIONES_APLICADAS.md](documentacion/CORRECCIONES_APLICADAS.md) | Bugs corregidos y mejoras |
| [RESUMEN_PROYECTO.md](documentacion/RESUMEN_PROYECTO.md) | Arquitectura y fases del proyecto |

---

## ⚙️ Tecnologías

- **Backend:** Spring Boot 3.4.5 + Spring Security + JWT
- **Frontend:** Thymeleaf + Bootstrap 5 + JavaScript vanilla
- **Base de datos:** MySQL 8.0
- **ORM:** Hibernate (JPA)
- **Tests:** JUnit 5 + Mockito + @SpringBootTest

---

## 🗂️ Estructura del proyecto

```
TFGFitApp/
├── src/main/java/com/tfgfitapp/tfgfitapp/
│   ├── config/           # Configuración (Security, CORS, DataInitializer)
│   ├── controller/       # REST API y controladores web
│   ├── dto/              # Data Transfer Objects
│   ├── entity/           # Entidades JPA
│   ├── enumeration/      # Enums (Role, ClientLevel, DayOfWeek)
│   ├── exception/        # Excepciones personalizadas
│   ├── repository/       # Repositorios Spring Data JPA
│   ├── security/         # JWT + UserDetailsService
│   └── service/          # Lógica de negocio
├── src/main/resources/
│   ├── templates/        # Vistas Thymeleaf
│   │   ├── fragments/    # Head, Sidebar
│   │   ├── dashboard/    # Páginas privadas
│   │   ├── index.html    # Landing page
│   │   ├── login.html
│   │   ├── register.html
│   │   └── error.html
│   ├── static/
│   │   ├── css/app.css   # Estilos deportivos
│   │   ├── js/app.js     # JWT + AJAX utilities
│   │   └── images/       # Favicon
│   └── application.properties
├── src/test/java/        # Tests unitarios e integración
├── documentacion/        # Guías y docs
├── cleanup-java.ps1      # Script limpieza Java
└── pom.xml
```

---

## 🎯 Funcionalidades

### 👤 Roles y permisos
- **ADMIN:** Gestión total del sistema, estadísticas globales
- **TRAINER:** Gestión de clientes, dietas, planes de entrenamiento
- **CLIENT:** Consulta sus planes, registra progreso

### 📊 Módulos principales
- ✅ Autenticación JWT (login/registro)
- ✅ Gestión de clientes (CRUD + asignación trainer)
- ✅ Dietas personalizadas (comidas + macros)
- ✅ Planes de entrenamiento (días + ejercicios)
- ✅ Registros de progreso (peso, medidas, historial)
- ✅ Exportación CSV del progreso
- ✅ Panel de administración
- ✅ Filtros y paginación

---

## 🧪 Tests

```powershell
# Ejecutar todos los tests
.\mvnw.cmd test

# Solo tests unitarios (Mockito)
.\mvnw.cmd test -Dtest="*ServiceTest"

# Solo tests de integración
.\mvnw.cmd test -Dtest="*ControllerTest"
```

**Cobertura actual:**
- 26 tests unitarios (Mockito)
- 29 tests de integración (@SpringBootTest)
- ✅ 0 fallos

---

## 🛠️ Configuración

### Base de datos
Edita `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/personal_trainer_manager
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD
```

### Puerto del servidor
```properties
server.port=8081
```

---

## 🐛 Solución de problemas

### Error: "Port 8081 already in use"
```powershell
.\cleanup-java.ps1
```

### MySQL no conecta
```powershell
# Verificar servicio
Get-Service -Name MySQL*

# Si está detenido
Start-Service -Name MySQL80
```

### Ver logs completos
```powershell
.\mvnw.cmd spring-boot:run -X
```

---

## 📦 Build para producción

```powershell
# Generar JAR
.\mvnw.cmd clean package -DskipTests

# El JAR estará en:
# target/TFGFitApp-0.0.1-SNAPSHOT.jar

# Ejecutar el JAR
java -jar target/TFGFitApp-0.0.1-SNAPSHOT.jar
```

---

## 📄 Licencia

Proyecto académico — TFG 2026

---

## 👨‍💻 Autor

Desarrollo completo del backend (Spring Boot + JWT + JPA) y frontend (Thymeleaf + Bootstrap).

---

## 📞 Soporte

Si tienes problemas:

1. Revisa [GUIA_ARRANQUE.md](documentacion/GUIA_ARRANQUE.md)
2. Ejecuta `.\cleanup-java.ps1` antes de arrancar
3. Verifica que MySQL esté corriendo
4. Consulta [PROBLEMA_MULTIPLES_INSTANCIAS.md](documentacion/PROBLEMA_MULTIPLES_INSTANCIAS.md)

