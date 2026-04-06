-- ============================================================================
-- SCRIPT COMPLETO DE BASE DE DATOS - TFGFitApp
-- ============================================================================
-- Versión FINAL CORREGIDA: Usa VARCHAR para compatibilidad con converters JPA
-- Fecha: 12/03/2026
-- ============================================================================

-- ============================================================================
-- 1. ELIMINAR BASE DE DATOS ANTERIOR (SI EXISTE)
-- ============================================================================
DROP DATABASE IF EXISTS personal_trainer_manager;

-- ============================================================================
-- 2. CREAR BASE DE DATOS NUEVA
-- ============================================================================
CREATE DATABASE personal_trainer_manager CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE personal_trainer_manager;

-- ============================================================================
-- 3. TABLA: users
-- ============================================================================
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 4. TABLA: trainers
-- ============================================================================
CREATE TABLE trainers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    phone VARCHAR(20),
    specialty VARCHAR(100),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_trainers_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 5. TABLA: clients
-- ============================================================================
CREATE TABLE clients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    trainer_id BIGINT,
    age INT,
    gender VARCHAR(20),
    height DECIMAL(5,2),
    weight DECIMAL(5,2),
    goal VARCHAR(150),
    level VARCHAR(20) DEFAULT 'beginner',
    injuries TEXT,
    allergies TEXT,
    notes TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_clients_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_clients_trainer
        FOREIGN KEY (trainer_id) REFERENCES trainers(id)
        ON DELETE SET NULL,
    INDEX idx_trainer (trainer_id),
    INDEX idx_level (level)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 6. TABLA: diets
-- ============================================================================
CREATE TABLE diets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    client_id BIGINT NOT NULL,
    trainer_id BIGINT NOT NULL,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    start_date DATE,
    end_date DATE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_diets_client
        FOREIGN KEY (client_id) REFERENCES clients(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_diets_trainer
        FOREIGN KEY (trainer_id) REFERENCES trainers(id)
        ON DELETE CASCADE,
    INDEX idx_client (client_id),
    INDEX idx_trainer (trainer_id),
    INDEX idx_active (active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 7. TABLA: diet_meals
-- ============================================================================
CREATE TABLE diet_meals (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    diet_id BIGINT NOT NULL,
    meal_type VARCHAR(50) NOT NULL,
    meal_time VARCHAR(20),
    foods TEXT NOT NULL,
    calories INT,
    notes TEXT,
    CONSTRAINT fk_diet_meals_diet
        FOREIGN KEY (diet_id) REFERENCES diets(id)
        ON DELETE CASCADE,
    INDEX idx_diet (diet_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 8. TABLA: workout_plans
-- ============================================================================
CREATE TABLE workout_plans (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    client_id BIGINT NOT NULL,
    trainer_id BIGINT NOT NULL,
    title VARCHAR(150) NOT NULL,
    objective VARCHAR(200),
    notes TEXT,
    start_date DATE,
    end_date DATE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_workout_plans_client
        FOREIGN KEY (client_id) REFERENCES clients(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_workout_plans_trainer
        FOREIGN KEY (trainer_id) REFERENCES trainers(id)
        ON DELETE CASCADE,
    INDEX idx_client (client_id),
    INDEX idx_trainer (trainer_id),
    INDEX idx_active (active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 9. TABLA: workout_days
-- ============================================================================
CREATE TABLE workout_days (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    workout_plan_id BIGINT NOT NULL,
    day_of_week VARCHAR(15) NOT NULL,
    focus VARCHAR(100),
    notes TEXT,
    CONSTRAINT fk_workout_days_plan
        FOREIGN KEY (workout_plan_id) REFERENCES workout_plans(id)
        ON DELETE CASCADE,
    INDEX idx_workout_plan (workout_plan_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 10. TABLA: exercises
-- ============================================================================
CREATE TABLE exercises (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    workout_day_id BIGINT NOT NULL,
    name VARCHAR(150) NOT NULL,
    sets INT,
    reps VARCHAR(50),
    rest_seconds INT,
    duration_minutes INT,
    notes TEXT,
    CONSTRAINT fk_exercises_day
        FOREIGN KEY (workout_day_id) REFERENCES workout_days(id)
        ON DELETE CASCADE,
    INDEX idx_workout_day (workout_day_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 11. TABLA: progress_records
-- ============================================================================
CREATE TABLE progress_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    client_id BIGINT NOT NULL,
    record_date DATE NOT NULL,
    weight DECIMAL(5,2),
    body_fat DECIMAL(5,2),
    chest DECIMAL(5,2),
    waist DECIMAL(5,2),
    hips DECIMAL(5,2),
    arms DECIMAL(5,2),
    legs DECIMAL(5,2),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_progress_client
        FOREIGN KEY (client_id) REFERENCES clients(id)
        ON DELETE CASCADE,
    INDEX idx_client (client_id),
    INDEX idx_date (record_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 12. INSERTAR DATOS DE PRUEBA
-- ============================================================================

-- USUARIOS (contraseña: password)
-- Hash BCrypt de "password": $2a$10$DGkXYOwgXGLSBVO1B.zdv.gLpF1W7OuqUfbEG8.UKB.53JIkoNBOm

-- ADMIN
INSERT INTO users (name, email, password, role, active, created_at) VALUES
('Administrador', 'admin@tfgfitapp.com', '$2a$10$DGkXYOwgXGLSBVO1B.zdv.gLpF1W7OuqUfbEG8.UKB.53JIkoNBOm', 'admin', true, NOW());

-- TRAINERS
INSERT INTO users (name, email, password, role, active, created_at) VALUES
('Carlos Ruiz', 'carlos.ruiz@tfgfitapp.com', '$2a$10$DGkXYOwgXGLSBVO1B.zdv.gLpF1W7OuqUfbEG8.UKB.53JIkoNBOm', 'trainer', true, NOW()),
('Laura Martínez', 'laura.martinez@tfgfitapp.com', '$2a$10$DGkXYOwgXGLSBVO1B.zdv.gLpF1W7OuqUfbEG8.UKB.53JIkoNBOm', 'trainer', true, NOW()),
('Miguel Sánchez', 'miguel.sanchez@tfgfitapp.com', '$2a$10$DGkXYOwgXGLSBVO1B.zdv.gLpF1W7OuqUfbEG8.UKB.53JIkoNBOm', 'trainer', true, NOW());

-- CLIENTS
INSERT INTO users (name, email, password, role, active, created_at) VALUES
('Juan Pérez', 'juan.perez@example.com', '$2a$10$DGkXYOwgXGLSBVO1B.zdv.gLpF1W7OuqUfbEG8.UKB.53JIkoNBOm', 'client', true, NOW()),
('Pedro Gómez', 'pedro.gomez@example.com', '$2a$10$DGkXYOwgXGLSBVO1B.zdv.gLpF1W7OuqUfbEG8.UKB.53JIkoNBOm', 'client', true, NOW()),
('Sergio López', 'sergio.lopez@example.com', '$2a$10$DGkXYOwgXGLSBVO1B.zdv.gLpF1W7OuqUfbEG8.UKB.53JIkoNBOm', 'client', true, NOW()),
('Ana García', 'ana.garcia@example.com', '$2a$10$DGkXYOwgXGLSBVO1B.zdv.gLpF1W7OuqUfbEG8.UKB.53JIkoNBOm', 'client', true, NOW()),
('María Rodríguez', 'maria.rodriguez@example.com', '$2a$10$DGkXYOwgXGLSBVO1B.zdv.gLpF1W7OuqUfbEG8.UKB.53JIkoNBOm', 'client', true, NOW()),
('Isabel Fernández', 'isabel.fernandez@example.com', '$2a$10$DGkXYOwgXGLSBVO1B.zdv.gLpF1W7OuqUfbEG8.UKB.53JIkoNBOm', 'client', true, NOW()),
('David Moreno', 'david.moreno@example.com', '$2a$10$DGkXYOwgXGLSBVO1B.zdv.gLpF1W7OuqUfbEG8.UKB.53JIkoNBOm', 'client', true, NOW()),
('Roberto Silva', 'roberto.silva@example.com', '$2a$10$DGkXYOwgXGLSBVO1B.zdv.gLpF1W7OuqUfbEG8.UKB.53JIkoNBOm', 'client', true, NOW()),
('Carmen Jiménez', 'carmen.jimenez@example.com', '$2a$10$DGkXYOwgXGLSBVO1B.zdv.gLpF1W7OuqUfbEG8.UKB.53JIkoNBOm', 'client', true, NOW());

-- TRAINERS (perfiles)
INSERT INTO trainers (user_id, phone, specialty, description, created_at) VALUES
(2, '634567890', 'Hipertrofia y Fuerza', 'Especialista en culturismo', NOW()),
(3, '645678901', 'Pérdida de Peso', 'Nutricionista certificada', NOW()),
(4, '656789012', 'CrossFit', 'Coach CrossFit Level 2', NOW());

-- CLIENTS (perfiles)
INSERT INTO clients (user_id, trainer_id, age, gender, height, weight, goal, level, injuries, allergies, notes, active, created_at) VALUES
(5, 1, 28, 'Masculino', 175.00, 78.00, 'Ganar masa muscular', 'intermediate', NULL, NULL, 'Cliente comprometido', true, NOW()),
(6, 1, 35, 'Masculino', 180.00, 85.00, 'Aumentar fuerza', 'advanced', NULL, NULL, 'Powerlifting', true, NOW()),
(7, 1, 24, 'Masculino', 172.00, 70.00, 'Empezar gimnasio', 'beginner', NULL, NULL, 'Primera vez', true, NOW()),
(8, 2, 30, 'Femenino', 165.00, 72.00, 'Perder grasa', 'beginner', NULL, 'Lactosa', NULL, true, NOW()),
(9, 2, 27, 'Femenino', 160.00, 65.00, 'Definición', 'intermediate', NULL, NULL, NULL, true, NOW()),
(10, 2, 42, 'Femenino', 158.00, 80.00, 'Bajar peso', 'beginner', 'Rodilla', 'Gluten', NULL, true, NOW()),
(11, 3, 26, 'Masculino', 178.00, 76.00, 'Rendimiento', 'advanced', NULL, NULL, 'CrossFit', true, NOW()),
(12, 3, 31, 'Masculino', 182.00, 88.00, 'Funcional', 'intermediate', NULL, NULL, NULL, true, NOW()),
(13, 3, 29, 'Femenino', 168.00, 63.00, 'Fuerza', 'intermediate', NULL, NULL, 'Atleta', true, NOW());

-- DIETS
INSERT INTO diets (client_id, trainer_id, title, description, start_date, end_date, active, created_at) VALUES
(1, 1, 'Hipertrofia 3500 kcal', 'Ganancia masa', '2026-02-01', '2026-04-30', true, NOW()),
(2, 1, 'Fuerza 3800 kcal', 'Powerlifting', '2026-02-15', '2026-05-15', true, NOW()),
(3, 1, 'Básico 2800 kcal', 'Principiantes', '2026-03-01', '2026-05-31', true, NOW()),
(4, 2, 'Pérdida 1600 kcal', 'Sin lactosa', '2026-02-10', '2026-05-10', true, NOW()),
(5, 2, 'Definición 1800 kcal', 'Mantener masa', '2026-02-01', '2026-04-30', true, NOW());

-- DIET MEALS
INSERT INTO diet_meals (diet_id, meal_type, meal_time, foods, calories, notes) VALUES
(1, 'Desayuno', '07:00', 'Avena, Proteína, Plátano', 650, NULL),
(1, 'Comida', '14:00', 'Arroz, Pollo, Verduras', 750, NULL),
(1, 'Cena', '21:00', 'Pasta, Salmón', 700, NULL),
(4, 'Desayuno', '08:00', 'Tortilla, Tostada', 300, NULL),
(4, 'Comida', '14:00', 'Ensalada, Pechuga', 450, NULL);

-- WORKOUT PLANS
INSERT INTO workout_plans (client_id, trainer_id, title, objective, notes, start_date, end_date, active, created_at) VALUES
(1, 1, 'Push/Pull/Legs', 'Masa muscular', 'Descanso 90seg', '2026-02-01', '2026-04-30', true, NOW()),
(2, 1, 'Powerlifting', 'Fuerza', 'Descanso 3-5min', '2026-02-15', '2026-05-15', true, NOW()),
(3, 1, 'Full Body', 'Técnica', 'Principiante', '2026-03-01', '2026-05-31', true, NOW());

-- WORKOUT DAYS
INSERT INTO workout_days (workout_plan_id, day_of_week, focus, notes) VALUES
(1, 'monday', 'Push', 'Pecho/Hombros'),
(1, 'tuesday', 'Pull', 'Espalda'),
(1, 'thursday', 'Legs', 'Piernas'),
(2, 'monday', 'Sentadilla', 'Fuerza'),
(2, 'wednesday', 'Press Banca', 'Fuerza'),
(3, 'monday', 'Full Body A', NULL),
(3, 'wednesday', 'Full Body B', NULL);

-- EXERCISES
INSERT INTO exercises (workout_day_id, name, sets, reps, rest_seconds, duration_minutes, notes) VALUES
(1, 'Press Banca', 4, '8-10', 120, NULL, NULL),
(1, 'Press Inclinado', 4, '10-12', 90, NULL, NULL),
(1, 'Press Militar', 3, '10', 90, NULL, NULL),
(2, 'Dominadas', 4, '8-10', 120, NULL, NULL),
(2, 'Remo Barra', 4, '8-10', 90, NULL, NULL),
(3, 'Sentadilla', 4, '8-10', 120, NULL, NULL),
(3, 'Prensa', 3, '12', 90, NULL, NULL);

-- PROGRESS RECORDS
INSERT INTO progress_records (client_id, record_date, weight, body_fat, chest, waist, hips, arms, legs, notes, created_at) VALUES
(1, '2026-02-01', 78.00, 16.0, 95.0, 82.0, 98.0, 35.0, 56.0, 'Inicio', NOW()),
(1, '2026-02-08', 78.50, 15.5, 96.0, 81.5, 98.5, 35.5, 56.5, 'Semana 1', NOW()),
(1, '2026-02-15', 79.00, 15.2, 96.5, 81.0, 99.0, 36.0, 57.0, 'Semana 2', NOW()),
(4, '2026-02-10', 72.00, 28.0, 92.0, 78.0, 102.0, 30.0, 54.0, 'Inicio', NOW()),
(4, '2026-02-17', 71.00, 27.2, 91.5, 77.0, 101.5, 29.8, 53.5, 'Semana 1', NOW());

-- ============================================================================
-- 13. VERIFICACIÓN
-- ============================================================================

SELECT '========================================' AS separador;
SELECT '✅ BASE DE DATOS CREADA CORRECTAMENTE' AS resultado;
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
SELECT '🔐 CREDENCIALES DE ACCESO' AS info;
SELECT '========================================' AS separador;
SELECT 'Email: admin@tfgfitapp.com' AS admin;
SELECT 'Password: password' AS password_admin;
SELECT '========================================' AS separador;
SELECT 'TODOS LOS USUARIOS TIENEN PASSWORD: password' AS nota;
SELECT '========================================' AS separador;

