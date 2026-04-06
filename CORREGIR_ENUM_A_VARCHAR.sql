-- ============================================================================
-- Script de CORRECCIÓN de Base de Datos - TFGFitApp
-- ============================================================================
-- Este script convierte las columnas ENUM a VARCHAR para usar converters JPA
-- EJECUTAR ESTE SCRIPT ANTES DE INICIAR LA APLICACIÓN
-- ============================================================================

USE personal_trainer_manager;

-- ============================================================================
-- MODIFICAR COLUMNAS DE ENUM A VARCHAR
-- ============================================================================

-- 1. Convertir role en users de ENUM a VARCHAR
ALTER TABLE users
MODIFY COLUMN role VARCHAR(20) NOT NULL;

-- 2. Convertir level en clients de ENUM a VARCHAR
ALTER TABLE clients
MODIFY COLUMN level VARCHAR(20) DEFAULT 'beginner';

-- 3. Convertir day_of_week en workout_days de ENUM a VARCHAR
ALTER TABLE workout_days
MODIFY COLUMN day_of_week VARCHAR(15) NOT NULL;

-- ============================================================================
-- VERIFICACIÓN
-- ============================================================================

SELECT '✅ Columnas convertidas de ENUM a VARCHAR correctamente' AS resultado;

-- Verificar estructura de users
DESC users;

-- Verificar estructura de clients
DESC clients;

-- Verificar estructura de workout_days
DESC workout_days;

SELECT '========================================' AS separador;
SELECT 'IMPORTANTE: Ahora ejecuta datos_prueba_SIMPLE.sql' AS siguiente_paso;
SELECT '========================================' AS separador;

