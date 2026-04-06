# 🎯 INSTRUCCIONES DE EJECUCIÓN

## PASO 1: Ejecutar el script SQL

1. Abre **MySQL Workbench**
2. Abre el archivo: `CREAR_BD_COMPLETA.sql`
3. Ejecuta TODO el script (Ctrl+Shift+Enter)
4. Verifica que se muestre: "✅ BASE DE DATOS CREADA CORRECTAMENTE"

## PASO 2: Confirmar

Cuando veas el mensaje de éxito en MySQL, escribe:

```
Ya he creado la base de datos
```

## PASO 3: Yo revisaré

Cuando me confirmes, yo revisaré automáticamente:
- ✅ Todas las entidades JPA
- ✅ Todos los repositorios
- ✅ Todos los servicios
- ✅ Todos los controladores
- ✅ Todas las templates
- ✅ Configuraciones

Y haré los ajustes necesarios para que TODO funcione correctamente.

---

## 📋 LO QUE HACE EL SCRIPT

1. ✅ Elimina la BD anterior (si existe)
2. ✅ Crea la BD nueva: `personal_trainer_manager`
3. ✅ Crea las 11 tablas con **VARCHAR** (NO ENUM)
4. ✅ Inserta datos de prueba completos
5. ✅ Verifica que todo se creó correctamente

---

## 🔐 CREDENCIALES

**TODOS los usuarios usan:** `password`

- Admin: admin@tfgfitapp.com
- Trainers: carlos.ruiz@tfgfitapp.com, laura.martinez@tfgfitapp.com, miguel.sanchez@tfgfitapp.com
- Clients: juan.perez@example.com, etc.

---

## ⚡ CAMBIOS CLAVE

El nuevo script usa **VARCHAR** en lugar de ENUM:
- `users.role` → VARCHAR(20) con valores: 'admin', 'trainer', 'client'
- `clients.level` → VARCHAR(20) con valores: 'beginner', 'intermediate', 'advanced'
- `workout_days.day_of_week` → VARCHAR(15) con valores: 'monday', 'tuesday', etc.

Esto es 100% compatible con los converters JPA que ya están en el código.

---

**Ejecuta el script y avísame cuando esté listo.**

