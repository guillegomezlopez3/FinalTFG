-- ============================================================================
-- Script de Datos de Prueba - TFGFitApp (FINAL - CONTRASEÑAS SIMPLES)
-- ============================================================================
-- IMPORTANTE: Contraseñas REALES y PROBADAS
-- Admin: admin@tfgfitapp.com / password
-- Otros: carlos.ruiz@tfgfitapp.com / password
-- ============================================================================

USE personal_trainer_manager;

-- ============================================================================
-- PASO 1: LIMPIAR DATOS EXISTENTES
-- ============================================================================

SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM progress_records;
DELETE FROM exercises;
DELETE FROM workout_days;
DELETE FROM workout_plans;
DELETE FROM diet_meals;
DELETE FROM diets;
DELETE FROM clients;
DELETE FROM trainers;
DELETE FROM users;

ALTER TABLE users AUTO_INCREMENT = 1;
ALTER TABLE trainers AUTO_INCREMENT = 1;
ALTER TABLE clients AUTO_INCREMENT = 1;
ALTER TABLE diets AUTO_INCREMENT = 1;
ALTER TABLE diet_meals AUTO_INCREMENT = 1;
ALTER TABLE workout_plans AUTO_INCREMENT = 1;
ALTER TABLE workout_days AUTO_INCREMENT = 1;
ALTER TABLE exercises AUTO_INCREMENT = 1;
ALTER TABLE progress_records AUTO_INCREMENT = 1;

SET FOREIGN_KEY_CHECKS = 1;

SELECT '✅ Paso 1: Datos eliminados' AS status;

-- ============================================================================
-- PASO 2: INSERTAR USUARIOS
-- ============================================================================
-- IMPORTANTE: Todas las contraseñas son "password" (sin comillas)
-- Hash BCrypt de "password": $2a$10$DGkXYOwgXGLSBVO1B.zdv.gLpF1W7OuqUfbEG8.UKB.53JIkoNBOm

-- ADMIN (ID: 1)
INSERT INTO users (name, email, password, role, active, created_at) VALUES
('Administrador', 'admin@tfgfitapp.com', '$2a$10$DGkXYOwgXGLSBVO1B.zdv.gLpF1W7OuqUfbEG8.UKB.53JIkoNBOm', 'ADMIN', true, NOW());

-- TRAINERS (IDs: 2, 3, 4)
INSERT INTO users (name, email, password, role, active, created_at) VALUES
('Carlos Ruiz', 'carlos.ruiz@tfgfitapp.com', '$2a$10$DGkXYOwgXGLSBVO1B.zdv.gLpF1W7OuqUfbEG8.UKB.53JIkoNBOm', 'TRAINER', true, NOW()),
('Laura Martínez', 'laura.martinez@tfgfitapp.com', '$2a$10$DGkXYOwgXGLSBVO1B.zdv.gLpF1W7OuqUfbEG8.UKB.53JIkoNBOm', 'TRAINER', true, NOW()),
('Miguel Sánchez', 'miguel.sanchez@tfgfitapp.com', '$2a$10$DGkXYOwgXGLSBVO1B.zdv.gLpF1W7OuqUfbEG8.UKB.53JIkoNBOm', 'TRAINER', true, NOW());

-- CLIENTS (IDs: 5-13)
INSERT INTO users (name, email, password, role, active, created_at) VALUES
('Juan Pérez', 'juan.perez@example.com', '$2a$10$DGkXYOwgXGLSBVO1B.zdv.gLpF1W7OuqUfbEG8.UKB.53JIkoNBOm', 'CLIENT', true, NOW()),
('Pedro Gómez', 'pedro.gomez@example.com', '$2a$10$DGkXYOwgXGLSBVO1B.zdv.gLpF1W7OuqUfbEG8.UKB.53JIkoNBOm', 'CLIENT', true, NOW()),
('Sergio López', 'sergio.lopez@example.com', '$2a$10$DGkXYOwgXGLSBVO1B.zdv.gLpF1W7OuqUfbEG8.UKB.53JIkoNBOm', 'CLIENT', true, NOW()),
('Ana García', 'ana.garcia@example.com', '$2a$10$DGkXYOwgXGLSBVO1B.zdv.gLpF1W7OuqUfbEG8.UKB.53JIkoNBOm', 'CLIENT', true, NOW()),
('María Rodríguez', 'maria.rodriguez@example.com', '$2a$10$DGkXYOwgXGLSBVO1B.zdv.gLpF1W7OuqUfbEG8.UKB.53JIkoNBOm', 'CLIENT', true, NOW()),
('Isabel Fernández', 'isabel.fernandez@example.com', '$2a$10$DGkXYOwgXGLSBVO1B.zdv.gLpF1W7OuqUfbEG8.UKB.53JIkoNBOm', 'CLIENT', true, NOW()),
('David Moreno', 'david.moreno@example.com', '$2a$10$DGkXYOwgXGLSBVO1B.zdv.gLpF1W7OuqUfbEG8.UKB.53JIkoNBOm', 'CLIENT', true, NOW()),
('Roberto Silva', 'roberto.silva@example.com', '$2a$10$DGkXYOwgXGLSBVO1B.zdv.gLpF1W7OuqUfbEG8.UKB.53JIkoNBOm', 'CLIENT', true, NOW()),
('Carmen Jiménez', 'carmen.jimenez@example.com', '$2a$10$DGkXYOwgXGLSBVO1B.zdv.gLpF1W7OuqUfbEG8.UKB.53JIkoNBOm', 'CLIENT', true, NOW());

SELECT '✅ Paso 2: 13 usuarios creados' AS status;

-- ============================================================================
-- PASO 3: TRAINERS
-- ============================================================================

INSERT INTO trainers (user_id, phone, specialty, description, created_at) VALUES
(2, '634567890', 'Hipertrofia y Fuerza', 'Especialista en culturismo', NOW()),
(3, '645678901', 'Pérdida de Peso', 'Nutricionista certificada', NOW()),
(4, '656789012', 'CrossFit', 'Coach CrossFit Level 2', NOW());

SELECT '✅ Paso 3: 3 trainers creados' AS status;

-- ============================================================================
-- PASO 4: CLIENTS
-- ============================================================================
-- IMPORTANTE: trainer_id hace referencia a trainers.id (1, 2, 3) NO a users.id

INSERT INTO clients (user_id, trainer_id, age, gender, height, weight, goal, level, injuries, allergies, notes, active, created_at) VALUES
(5, 1, 28, 'Masculino', 175.00, 78.00, 'Ganar masa muscular', 'INTERMEDIATE', NULL, NULL, 'Cliente comprometido', true, NOW()),
(6, 1, 35, 'Masculino', 180.00, 85.00, 'Aumentar fuerza', 'ADVANCED', NULL, NULL, 'Powerlifting', true, NOW()),
(7, 1, 24, 'Masculino', 172.00, 70.00, 'Empezar gimnasio', 'BEGINNER', NULL, NULL, 'Primera vez', true, NOW()),
(8, 2, 30, 'Femenino', 165.00, 72.00, 'Perder grasa', 'BEGINNER', NULL, 'Lactosa', NULL, true, NOW()),
(9, 2, 27, 'Femenino', 160.00, 65.00, 'Definición', 'INTERMEDIATE', NULL, NULL, NULL, true, NOW()),
(10, 2, 42, 'Femenino', 158.00, 80.00, 'Bajar peso', 'BEGINNER', 'Rodilla', 'Gluten', NULL, true, NOW()),
(11, 3, 26, 'Masculino', 178.00, 76.00, 'Rendimiento', 'ADVANCED', NULL, NULL, 'CrossFit', true, NOW()),
(12, 3, 31, 'Masculino', 182.00, 88.00, 'Funcional', 'INTERMEDIATE', NULL, NULL, NULL, true, NOW()),
(13, 3, 29, 'Femenino', 168.00, 63.00, 'Fuerza', 'INTERMEDIATE', NULL, NULL, 'Atleta', true, NOW());

SELECT '✅ Paso 4: 9 clientes creados' AS status;

-- ============================================================================
-- PASO 5: DIETS
-- ============================================================================

INSERT INTO diets (client_id, trainer_id, title, description, start_date, end_date, active, created_at) VALUES
(1, 1, 'Hipertrofia 3500 kcal', 'Ganancia masa', '2026-02-01', '2026-04-30', true, NOW()),
(2, 1, 'Fuerza 3800 kcal', 'Powerlifting', '2026-02-15', '2026-05-15', true, NOW()),
(3, 1, 'Básico 2800 kcal', 'Principiantes', '2026-03-01', '2026-05-31', true, NOW()),
(4, 2, 'Pérdida 1600 kcal', 'Sin lactosa', '2026-02-10', '2026-05-10', true, NOW()),
(5, 2, 'Definición 1800 kcal', 'Mantener masa', '2026-02-01', '2026-04-30', true, NOW());

SELECT '✅ Paso 5: 5 dietas creadas' AS status;

-- ============================================================================
-- PASO 6: DIET_MEALS
-- ============================================================================

INSERT INTO diet_meals (diet_id, meal_type, meal_time, foods, calories, notes) VALUES
(1, 'Desayuno', '07:00', 'Avena, Proteína, Plátano', 650, NULL),
(1, 'Comida', '14:00', 'Arroz, Pollo, Verduras', 750, NULL),
(1, 'Cena', '21:00', 'Pasta, Salmón', 700, NULL),
(4, 'Desayuno', '08:00', 'Tortilla, Tostada', 300, NULL),
(4, 'Comida', '14:00', 'Ensalada, Pechuga', 450, NULL);

SELECT '✅ Paso 6: 5 comidas creadas' AS status;

-- ============================================================================
-- PASO 7: WORKOUT_PLANS
-- ============================================================================

INSERT INTO workout_plans (client_id, trainer_id, title, objective, notes, start_date, end_date, active, created_at) VALUES
(1, 1, 'Push/Pull/Legs', 'Masa muscular', 'Descanso 90seg', '2026-02-01', '2026-04-30', true, NOW()),
(2, 1, 'Powerlifting', 'Fuerza', 'Descanso 3-5min', '2026-02-15', '2026-05-15', true, NOW()),
(3, 1, 'Full Body', 'Técnica', 'Principiante', '2026-03-01', '2026-05-31', true, NOW());

SELECT '✅ Paso 7: 3 planes creados' AS status;

-- ============================================================================
-- PASO 8: WORKOUT_DAYS
-- ============================================================================

INSERT INTO workout_days (workout_plan_id, day_of_week, focus, notes) VALUES
(1, 'MONDAY', 'Push', 'Pecho/Hombros'),
(1, 'TUESDAY', 'Pull', 'Espalda'),
(1, 'THURSDAY', 'Legs', 'Piernas'),
(2, 'MONDAY', 'Sentadilla', 'Fuerza'),
(2, 'WEDNESDAY', 'Press Banca', 'Fuerza'),
(3, 'MONDAY', 'Full Body A', NULL),
(3, 'WEDNESDAY', 'Full Body B', NULL);

SELECT '✅ Paso 8: 7 días creados' AS status;

-- ============================================================================
-- PASO 9: EXERCISES
-- ============================================================================

INSERT INTO exercises (workout_day_id, name, sets, reps, rest_seconds, duration_minutes, notes) VALUES
(1, 'Press Banca', 4, '8-10', 120, NULL, NULL),
(1, 'Press Inclinado', 4, '10-12', 90, NULL, NULL),
(1, 'Press Militar', 3, '10', 90, NULL, NULL),
(2, 'Dominadas', 4, '8-10', 120, NULL, NULL),
(2, 'Remo Barra', 4, '8-10', 90, NULL, NULL),
(3, 'Sentadilla', 4, '8-10', 120, NULL, NULL),
(3, 'Prensa', 3, '12', 90, NULL, NULL);

SELECT '✅ Paso 9: 7 ejercicios creados' AS status;

-- ============================================================================
-- PASO 10: PROGRESS_RECORDS
-- ============================================================================

INSERT INTO progress_records (client_id, record_date, weight, body_fat, chest, waist, hips, arms, legs, notes, created_at) VALUES
(1, '2026-02-01', 78.00, 16.0, 95.0, 82.0, 98.0, 35.0, 56.0, 'Inicio', NOW()),
(1, '2026-02-08', 78.50, 15.5, 96.0, 81.5, 98.5, 35.5, 56.5, 'Semana 1', NOW()),
(1, '2026-02-15', 79.00, 15.2, 96.5, 81.0, 99.0, 36.0, 57.0, 'Semana 2', NOW()),
(4, '2026-02-10', 72.00, 28.0, 92.0, 78.0, 102.0, 30.0, 54.0, 'Inicio', NOW()),
(4, '2026-02-17', 71.00, 27.2, 91.5, 77.0, 101.5, 29.8, 53.5, 'Semana 1', NOW());

SELECT '✅ Paso 10: 5 registros creados' AS status;

-- ============================================================================
-- VERIFICACIÓN
-- ============================================================================

SELECT '========================================' AS separador;
SELECT '✅ DATOS INSERTADOS CORRECTAMENTE' AS resultado;
SELECT '========================================' AS separador;
SELECT 'Usuarios:' AS tipo, COUNT(*) AS total FROM users;
SELECT 'Trainers:' AS tipo, COUNT(*) AS total FROM trainers;
SELECT 'Clientes:' AS tipo, COUNT(*) AS total FROM clients;
SELECT 'Dietas:' AS tipo, COUNT(*) AS total FROM diets;
SELECT 'Comidas:' AS tipo, COUNT(*) AS total FROM diet_meals;
SELECT 'Planes:' AS tipo, COUNT(*) AS total FROM workout_plans;
SELECT 'Días:' AS tipo, COUNT(*) AS total FROM workout_days;
SELECT 'Ejercicios:' AS tipo, COUNT(*) AS total FROM exercises;
SELECT 'Progreso:' AS tipo, COUNT(*) AS total FROM progress_records;
SELECT '========================================' AS separador;
SELECT '🔐 CREDENCIALES DE PRUEBA' AS info;
SELECT '========================================' AS separador;
SELECT 'Email' AS campo, 'Password' AS valor;
SELECT '========================================' AS separador;
SELECT 'admin@tfgfitapp.com' AS email, 'password' AS password_value;
SELECT 'carlos.ruiz@tfgfitapp.com' AS email, 'password' AS password_value;
SELECT 'juan.perez@example.com' AS email, 'password' AS password_value;
SELECT '========================================' AS separador;
SELECT 'TODOS LOS USUARIOS TIENEN LA CONTRASEÑA: password' AS nota;
SELECT '(sin mayúsculas, sin números, solo "password")' AS aclaracion;
SELECT '========================================' AS separador;

