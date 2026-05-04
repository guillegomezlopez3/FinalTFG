package com.tfgfitapp.tfgfitapp.config;

import com.tfgfitapp.tfgfitapp.entity.PredefinedExercise;
import com.tfgfitapp.tfgfitapp.enumeration.MuscleGroup;
import com.tfgfitapp.tfgfitapp.repository.PredefinedExerciseRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class PredefinedExerciseInitializer {

    public PredefinedExerciseInitializer(PredefinedExerciseRepository repository) {
        this.repository = repository;
    }

    private static final Logger log = LoggerFactory.getLogger(PredefinedExerciseInitializer.class);
    private final PredefinedExerciseRepository repository;

    @Bean
    @Order(2)
    public CommandLineRunner initPredefinedExercises() {
        return args -> {
            if (repository.count() == 0) {
                log.info("Inicializando catalogo de ejercicios...");
                List<PredefinedExercise> list = new ArrayList<>();

                // PECHO
                list.add(new PredefinedExercise(null, "Press de Banca Plato", MuscleGroup.CHEST, "Barra"));
                list.add(new PredefinedExercise(null, "Press Inclinado", MuscleGroup.CHEST, "Mancuernas"));
                list.add(new PredefinedExercise(null, "Aperturas", MuscleGroup.CHEST, "Mancuernas"));
                list.add(new PredefinedExercise(null, "Cruces en Polea", MuscleGroup.CHEST, "Polea"));
                list.add(new PredefinedExercise(null, "Fondos en Paralelas", MuscleGroup.CHEST, "Peso Corporal"));
                list.add(new PredefinedExercise(null, "Press Declinado", MuscleGroup.CHEST, "Barra"));
                list.add(new PredefinedExercise(null, "Peck Deck", MuscleGroup.CHEST, "Maquina"));
                list.add(new PredefinedExercise(null, "Flexiones (Push-ups)", MuscleGroup.CHEST, "Peso Corporal"));
                list.add(new PredefinedExercise(null, "Press en Maquina Convergente", MuscleGroup.CHEST, "Maquina"));

                // ESPALDA
                list.add(new PredefinedExercise(null, "Dominadas", MuscleGroup.BACK, "Peso Corporal"));
                list.add(new PredefinedExercise(null, "Jalon al Pecho", MuscleGroup.BACK, "Polea"));
                list.add(new PredefinedExercise(null, "Remo con Barra", MuscleGroup.BACK, "Barra"));
                list.add(new PredefinedExercise(null, "Remo en Punta", MuscleGroup.BACK, "Barra T"));
                list.add(new PredefinedExercise(null, "Remo con Mancuerna a una mano", MuscleGroup.BACK, "Mancuerna"));
                list.add(new PredefinedExercise(null, "Remo Gironda", MuscleGroup.BACK, "Polea"));
                list.add(new PredefinedExercise(null, "Pull-over", MuscleGroup.BACK, "Mancuerna"));
                list.add(new PredefinedExercise(null, "Peso Muerto", MuscleGroup.BACK, "Barra"));

                // PIERNAS
                list.add(new PredefinedExercise(null, "Sentadilla Libre", MuscleGroup.LEGS, "Barra"));
                list.add(new PredefinedExercise(null, "Sentadilla Hack", MuscleGroup.LEGS, "Maquina"));
                list.add(new PredefinedExercise(null, "Prensa Inclinada", MuscleGroup.LEGS, "Maquina"));
                list.add(new PredefinedExercise(null, "Zancadas (Lunges)", MuscleGroup.LEGS, "Mancuernas"));
                list.add(new PredefinedExercise(null, "Peso Muerto Rumano", MuscleGroup.LEGS, "Barra"));
                list.add(new PredefinedExercise(null, "Extensiones de Cuadriceps", MuscleGroup.LEGS, "Maquina"));
                list.add(new PredefinedExercise(null, "Curl Femoral Tumbado", MuscleGroup.LEGS, "Maquina"));
                list.add(new PredefinedExercise(null, "Curl Femoral Sentado", MuscleGroup.LEGS, "Maquina"));
                list.add(new PredefinedExercise(null, "Elevacion de Gemelos de Pie", MuscleGroup.LEGS, "Maquina"));
                list.add(new PredefinedExercise(null, "Elevacion de Gemelos Sentado", MuscleGroup.LEGS, "Maquina"));
                list.add(new PredefinedExercise(null, "Hip Thrust", MuscleGroup.LEGS, "Barra"));
                list.add(new PredefinedExercise(null, "Abductores", MuscleGroup.LEGS, "Maquina"));
                list.add(new PredefinedExercise(null, "Aductores", MuscleGroup.LEGS, "Maquina"));

                // HOMBROS
                list.add(new PredefinedExercise(null, "Press Militar", MuscleGroup.SHOULDERS, "Barra"));
                list.add(new PredefinedExercise(null, "Press Arnold", MuscleGroup.SHOULDERS, "Mancuernas"));
                list.add(new PredefinedExercise(null, "Elevaciones Laterales", MuscleGroup.SHOULDERS, "Mancuernas"));
                list.add(new PredefinedExercise(null, "Elevaciones Frontales", MuscleGroup.SHOULDERS, "Mancuernas"));
                list.add(new PredefinedExercise(null, "Pajaros (Elevaciones Posteriores)", MuscleGroup.SHOULDERS, "Mancuernas"));
                list.add(new PredefinedExercise(null, "Face Pull", MuscleGroup.SHOULDERS, "Polea"));
                list.add(new PredefinedExercise(null, "Encogimientos para Trapecios", MuscleGroup.SHOULDERS, "Mancuernas"));

                // BRAZOS (Biceps / Triceps)
                list.add(new PredefinedExercise(null, "Curl de Biceps con Barra", MuscleGroup.ARMS, "Barra"));
                list.add(new PredefinedExercise(null, "Curl Alterno", MuscleGroup.ARMS, "Mancuernas"));
                list.add(new PredefinedExercise(null, "Curl Martillo", MuscleGroup.ARMS, "Mancuernas"));
                list.add(new PredefinedExercise(null, "Curl Predicador", MuscleGroup.ARMS, "Barra Z"));
                list.add(new PredefinedExercise(null, "Curl en Polea Baja", MuscleGroup.ARMS, "Polea"));
                list.add(new PredefinedExercise(null, "Extension de Triceps en Polea", MuscleGroup.ARMS, "Polea"));
                list.add(new PredefinedExercise(null, "Press Frances", MuscleGroup.ARMS, "Barra Z"));
                list.add(new PredefinedExercise(null, "Fondos entre Bancos", MuscleGroup.ARMS, "Peso Corporal"));
                list.add(new PredefinedExercise(null, "Patada de Triceps", MuscleGroup.ARMS, "Mancuerna"));
                list.add(new PredefinedExercise(null, "Extension Tras Nuca", MuscleGroup.ARMS, "Mancuerna"));

                // CORE
                list.add(new PredefinedExercise(null, "Crunch Abdominal", MuscleGroup.CORE, "Peso Corporal"));
                list.add(new PredefinedExercise(null, "Plancha (Plank)", MuscleGroup.CORE, "Peso Corporal"));
                list.add(new PredefinedExercise(null, "Elevacion de Piernas", MuscleGroup.CORE, "Peso Corporal"));
                list.add(new PredefinedExercise(null, "Rueda Abdominal", MuscleGroup.CORE, "Rueda"));
                list.add(new PredefinedExercise(null, "Twist Ruso", MuscleGroup.CORE, "Disco/Balon"));
                list.add(new PredefinedExercise(null, "Crunch en Polea Alta", MuscleGroup.CORE, "Polea"));

                // CARDIO
                list.add(new PredefinedExercise(null, "Cinta de Correr", MuscleGroup.CARDIO, "Maquina"));
                list.add(new PredefinedExercise(null, "Bicicleta Estatica", MuscleGroup.CARDIO, "Maquina"));
                list.add(new PredefinedExercise(null, "Elíptica", MuscleGroup.CARDIO, "Maquina"));
                list.add(new PredefinedExercise(null, "Remo Ergometro", MuscleGroup.CARDIO, "Maquina"));
                list.add(new PredefinedExercise(null, "Saltar a la Comba", MuscleGroup.CARDIO, "Comba"));
                list.add(new PredefinedExercise(null, "Burpees", MuscleGroup.CARDIO, "Peso Corporal"));

                // FULL BODY / CROSSFIT
                list.add(new PredefinedExercise(null, "Kettlebell Swing", MuscleGroup.FULL_BODY, "Pesa Rusa"));
                list.add(new PredefinedExercise(null, "Clean and Jerk", MuscleGroup.FULL_BODY, "Barra"));
                list.add(new PredefinedExercise(null, "Snatch", MuscleGroup.FULL_BODY, "Barra"));

                repository.saveAll(list);
                log.info("Catalogo de ejercicios guardado. ({})", list.size());
            }
        };
    }
}
