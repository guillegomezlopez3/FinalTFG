-- ============================================================================
-- Script de Creación de Base de Datos - TFGFitApp
-- ============================================================================
-- Versión CORREGIDA con ENUMs en MAYÚSCULAS para compatibilidad con Java
-- Fecha: 2026-03-12
-- ============================================================================

CREATE DATABASE IF NOT EXISTS personal_trainer_manager;
USE personal_trainer_manager;

-- ============================================================================
-- Tabla: users
-- ============================================================================
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'TRAINER', 'CLIENT') NOT NULL,  -- ✅ MAYÚSCULAS
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- Tabla: trainers
-- ============================================================================
CREATE TABLE IF NOT EXISTS trainers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    phone VARCHAR(20),
    specialty VARCHAR(100),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_trainers_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE
);

-- ============================================================================
-- Tabla: clients
-- ============================================================================
CREATE TABLE IF NOT EXISTS clients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    trainer_id BIGINT,
    age INT,
    gender VARCHAR(20),
    height DECIMAL(5,2),
    weight DECIMAL(5,2),
    goal VARCHAR(150),
    level ENUM('BEGINNER', 'INTERMEDIATE', 'ADVANCED') DEFAULT 'BEGINNER',  -- ✅ MAYÚSCULAS
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
        ON DELETE SET NULL
);

-- ============================================================================
-- Tabla: diets
-- ============================================================================
CREATE TABLE IF NOT EXISTS diets (
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
        ON DELETE CASCADE
);

-- ============================================================================
-- Tabla: diet_meals
-- ============================================================================
CREATE TABLE IF NOT EXISTS diet_meals (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    diet_id BIGINT NOT NULL,
    meal_type VARCHAR(50) NOT NULL,
    meal_time VARCHAR(20),
    foods TEXT NOT NULL,
    calories INT,
    notes TEXT,
    CONSTRAINT fk_diet_meals_diet
        FOREIGN KEY (diet_id) REFERENCES diets(id)
        ON DELETE CASCADE
);

-- ============================================================================
-- Tabla: workout_plans
-- ============================================================================
CREATE TABLE IF NOT EXISTS workout_plans (
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
        ON DELETE CASCADE
);

-- ============================================================================
-- Tabla: workout_days
-- ============================================================================
CREATE TABLE IF NOT EXISTS workout_days (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    workout_plan_id BIGINT NOT NULL,
    day_of_week ENUM('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY') NOT NULL,  -- ✅ MAYÚSCULAS
    focus VARCHAR(100),
    notes TEXT,
    CONSTRAINT fk_workout_days_plan
        FOREIGN KEY (workout_plan_id) REFERENCES workout_plans(id)
        ON DELETE CASCADE
);

-- ============================================================================
-- Tabla: exercises
-- ============================================================================
CREATE TABLE IF NOT EXISTS exercises (
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
        ON DELETE CASCADE
);

-- ============================================================================
-- Tabla: progress_records
-- ============================================================================
CREATE TABLE IF NOT EXISTS progress_records (
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
        ON DELETE CASCADE
);

-- ============================================================================
-- Mensaje Final
-- ============================================================================
SELECT '✅ Base de datos creada correctamente con ENUMs en MAYÚSCULAS' AS resultado;
SELECT 'El usuario ADMIN se creará automáticamente al arrancar Spring Boot' AS info;
SELECT 'Credenciales por defecto: admin@tfgfitapp.com / Admin1234!' AS credenciales;

