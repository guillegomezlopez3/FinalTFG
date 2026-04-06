-- ============================================================================
-- Script de Corrección Completa: Base de Datos TFGFitApp
-- ============================================================================
-- Este script:
-- 1. Elimina todos los datos existentes
-- 2. Actualiza los ENUMs a mayúsculas
-- 3. Deja la BD lista para que Spring Boot cree el usuario ADMIN correctamente
-- ============================================================================

USE personal_trainer_manager;

-- ============================================================================
-- PASO 1: Eliminar todos los datos existentes (CUIDADO: Esto borra todo)
-- ============================================================================

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE progress_records;
TRUNCATE TABLE exercises;
TRUNCATE TABLE workout_days;
TRUNCATE TABLE workout_plans;
TRUNCATE TABLE diet_meals;
TRUNCATE TABLE diets;
TRUNCATE TABLE clients;
TRUNCATE TABLE trainers;
TRUNCATE TABLE users;

SET FOREIGN_KEY_CHECKS = 1;

SELECT 'Paso 1 completado: Todos los datos eliminados' AS status;

-- ============================================================================
-- PASO 2: Actualizar ENUMs a mayúsculas
-- ============================================================================

-- Tabla: users - columna role
ALTER TABLE users
MODIFY COLUMN role ENUM('ADMIN', 'TRAINER', 'CLIENT') NOT NULL;

-- Tabla: clients - columna level
ALTER TABLE clients
MODIFY COLUMN level ENUM('BEGINNER', 'INTERMEDIATE', 'ADVANCED') DEFAULT 'BEGINNER';

-- Tabla: workout_days - columna day_of_week
ALTER TABLE workout_days
MODIFY COLUMN day_of_week ENUM('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY') NOT NULL;

SELECT 'Paso 2 completado: ENUMs actualizados a mayúsculas' AS status;

-- ============================================================================
-- VERIFICACIÓN FINAL
-- ============================================================================

-- Verificar que no hay usuarios
SELECT 'Usuarios en BD:' AS info, COUNT(*) AS total FROM users;

-- Mostrar la estructura actualizada de users
SHOW CREATE TABLE users;

-- Mensaje final
SELECT '✅ Base de datos lista para arrancar la aplicación' AS resultado;
SELECT 'El usuario ADMIN se creará automáticamente al arrancar Spring Boot' AS info;

