# 📊 Guía - Datos de Prueba

## 🎯 Qué Incluye el Script

El script `datos_prueba.sql` crea:

### 👥 Usuarios (13 en total)
- ✅ **1 Administrador**
- ✅ **3 Entrenadores** con perfiles completos
- ✅ **9 Clientes** (3 por cada entrenador)

### 💪 Datos de Entrenamiento
- ✅ **9 Dietas** personalizadas (una por cliente)
- ✅ **20+ Comidas** detalladas
- ✅ **5 Planes de entrenamiento** completos
- ✅ **17 Días de entrenamiento** programados
- ✅ **20+ Ejercicios** con series y repeticiones

### 📈 Registros de Progreso
- ✅ **16 Registros** de evolución física
- ✅ Datos realistas de peso, grasa, medidas

---

## 🚀 Cómo Ejecutar el Script

### **IMPORTANTE: Orden de Ejecución**

**1. PRIMERO:** Ejecuta `reset_database.sql` (solo si no lo has hecho)
```sql
-- Esto corrige los ENUMs y limpia la base de datos
```

**2. DESPUÉS:** Ejecuta `datos_prueba.sql` (este script)
```sql
-- Esto inserta todos los datos de ejemplo
```

---

## 📝 Opción A - MySQL Workbench (RECOMENDADO)

### Paso 1: Abrir MySQL Workbench
1. Abre **MySQL Workbench**
2. Conéctate a tu servidor local
   - Host: `localhost`
   - Port: `3306`
   - User: `root`
   - Password: `Pipas1372.`

### Paso 2: Abrir el Script
1. **File** → **Open SQL Script...**
2. Navega a: `datos_prueba.sql`
3. El script se abrirá en el editor

### Paso 3: Ejecutar
1. Click en el botón **⚡ Execute** (rayo)
2. O presiona **Ctrl + Shift + Enter**

### Paso 4: Verificar
Deberías ver al final:
```
✅ Datos de prueba insertados correctamente
Usuarios creados: 13
Trainers creados: 3
Clients creados: 9
Dietas creadas: 9
Comidas creadas: 20+
Planes entrenamiento: 5
...
```

---

## 📝 Opción B - Línea de Comandos

```powershell
# 1. Navega al directorio del proyecto
cd "C:\Users\USUARIO\OneDrive - fempa.es\Escritorio\AccesoAdatosPracticaFinal\TFGFitApp"

# 2. Ejecuta el script
mysql -u root -p personal_trainer_manager < datos_prueba.sql

# 3. Cuando pida password: Pipas1372.
```

---

## 🔐 Credenciales de Prueba

### ADMIN
```
Email: admin@tfgfitapp.com
Password: Admin1234!
```

### TRAINERS (Todos con password: Password123!)
```
1. carlos.ruiz@tfgfitapp.com
   - Especialidad: Hipertrofia y Fuerza
   - Tiene 3 clientes: Juan, Pedro, Sergio

2. laura.martinez@tfgfitapp.com
   - Especialidad: Pérdida de Peso
   - Tiene 3 clientes: Ana, María, Isabel

3. miguel.sanchez@tfgfitapp.com
   - Especialidad: CrossFit y Funcional
   - Tiene 3 clientes: David, Roberto, Carmen
```

### CLIENTS (Todos con password: Password123!)
```
Juan Pérez       - juan.perez@example.com       (Trainer: Carlos)
Pedro Gómez      - pedro.gomez@example.com      (Trainer: Carlos)
Sergio López     - sergio.lopez@example.com     (Trainer: Carlos)
Ana García       - ana.garcia@example.com       (Trainer: Laura)
María Rodríguez  - maria.rodriguez@example.com  (Trainer: Laura)
Isabel Fernández - isabel.fernandez@example.com (Trainer: Laura)
David Moreno     - david.moreno@example.com     (Trainer: Miguel)
Roberto Silva    - roberto.silva@example.com    (Trainer: Miguel)
Carmen Jiménez   - carmen.jimenez@example.com   (Trainer: Miguel)
```

---

## 🎯 Qué Puedes Probar

### Como ADMIN (admin@tfgfitapp.com)
- ✅ Ver estadísticas globales
- ✅ Ver todos los usuarios
- ✅ Gestionar el sistema

### Como TRAINER (ej: carlos.ruiz@tfgfitapp.com)
- ✅ Ver mis 3 clientes
- ✅ Ver/editar sus dietas
- ✅ Ver/editar sus planes de entrenamiento
- ✅ Ver su progreso físico

### Como CLIENT (ej: juan.perez@example.com)
- ✅ Ver mi perfil
- ✅ Ver mi dieta con todas las comidas
- ✅ Ver mi plan de entrenamiento con ejercicios
- ✅ Ver mi evolución de progreso

---

## ⚠️ Notas Importantes

### 1. Contraseñas
- **ADMIN:** `Admin1234!`
- **Todos los demás:** `Password123!`

Las contraseñas están cifradas con BCrypt en la base de datos.

### 2. Relaciones
Todos los datos están correctamente relacionados:
- ✅ Cada cliente tiene un trainer asignado
- ✅ Cada dieta pertenece a un cliente y trainer
- ✅ Cada plan de entrenamiento tiene días y ejercicios
- ✅ Los registros de progreso están vinculados a clientes

### 3. Datos Realistas
Los datos son coherentes y realistas:
- ✅ Pesos, alturas, objetivos reales
- ✅ Dietas con calorías apropiadas
- ✅ Planes de entrenamiento variados
- ✅ Progreso físico gradual

---

## 🔄 Si Necesitas Resetear

Si quieres volver a empezar desde cero:

```sql
-- 1. Ejecuta reset_database.sql (borra todo y corrige ENUMs)
-- 2. Ejecuta datos_prueba.sql (inserta datos de nuevo)
```

---

## 📊 Resumen de Datos

| Tabla | Cantidad | Descripción |
|-------|----------|-------------|
| users | 13 | 1 admin + 3 trainers + 9 clients |
| trainers | 3 | Perfiles de entrenadores |
| clients | 9 | Perfiles de clientes con objetivos |
| diets | 9 | Una dieta por cliente |
| diet_meals | 20+ | Comidas detalladas |
| workout_plans | 5 | Planes variados |
| workout_days | 17 | Días programados |
| exercises | 20+ | Ejercicios con detalles |
| progress_records | 16 | Evolución física |

---

## ✅ Verificación

Para verificar que todo se insertó correctamente:

```sql
USE personal_trainer_manager;

-- Ver todos los usuarios
SELECT id, name, email, role FROM users;

-- Ver clientes con sus trainers
SELECT c.id, u.name AS client_name, t.specialty 
FROM clients c
JOIN users u ON c.user_id = u.id
JOIN trainers tr ON c.trainer_id = tr.id
JOIN users t ON tr.user_id = t.id;

-- Ver dietas con cliente
SELECT d.title, u.name AS client_name, d.active
FROM diets d
JOIN clients c ON d.client_id = c.id
JOIN users u ON c.user_id = u.id;
```

---

**Fecha:** 2026-03-12  
**Archivo:** `datos_prueba.sql`  
**Estado:** ✅ Listo para usar

