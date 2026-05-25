package com.tfgfitapp.tfgfitapp.config;

import com.tfgfitapp.tfgfitapp.entity.*;
import com.tfgfitapp.tfgfitapp.enumeration.*;
import com.tfgfitapp.tfgfitapp.repository.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Inicializador del catálogo de ejercicios predefinidos, rutinas y dietas.
 */
@Configuration
public class PredefinedExerciseInitializer {

    private static final Logger log = LoggerFactory.getLogger(PredefinedExerciseInitializer.class);
    private final PredefinedExerciseRepository repository;
    private final ExerciseRepository exerciseRepository;
    private final ClientRepository clientRepository;
    private final TrainerRepository trainerRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final DietRepository dietRepository;

    /**
     * Constructor inyectable con todos los repositorios requeridos.
     */
    public PredefinedExerciseInitializer(
            PredefinedExerciseRepository repository,
            ExerciseRepository exerciseRepository,
            ClientRepository clientRepository,
            TrainerRepository trainerRepository,
            WorkoutPlanRepository workoutPlanRepository,
            DietRepository dietRepository) {
        this.repository = repository;
        this.exerciseRepository = exerciseRepository;
        this.clientRepository = clientRepository;
        this.trainerRepository = trainerRepository;
        this.workoutPlanRepository = workoutPlanRepository;
        this.dietRepository = dietRepository;
    }

    /**
     * Define un CommandLineRunner que se ejecuta al iniciar el contexto de Spring.
     * 
     * @return Tarea de inicialización del catálogo de ejercicios.
     */
    @Bean
    @Order(2)
    public CommandLineRunner initPredefinedExercises() {
        return args -> {
            if (repository.count() == 0) {
                log.info("Inicializando catalogo de ejercicios...");
                List<PredefinedExercise> list = new ArrayList<>();

                // PECHO
                list.add(new PredefinedExercise(null, "Press de Banca Plano", MuscleGroup.CHEST, "Barra",
                        "/images/Pecho/Press de Banca Plano.png"));
                list.add(new PredefinedExercise(null, "Press Inclinado", MuscleGroup.CHEST, "Mancuernas",
                        "/images/Pecho/Press Inclinado Mancuernas.png"));
                list.add(new PredefinedExercise(null, "Aperturas", MuscleGroup.CHEST, "Mancuernas",
                        "/images/Pecho/Aperturas.png"));
                list.add(new PredefinedExercise(null, "Cruces en Polea", MuscleGroup.CHEST, "Polea",
                        "/images/Pecho/Cruces en Polea.png"));
                list.add(new PredefinedExercise(null, "Fondos en Paralelas", MuscleGroup.CHEST, "Peso Corporal",
                        "/images/Pecho/Fondos En Paralelas.png"));
                list.add(new PredefinedExercise(null, "Press Declinado", MuscleGroup.CHEST, "Barra",
                        "/images/Pecho/Press Declinado.png"));
                list.add(new PredefinedExercise(null, "Peck Deck", MuscleGroup.CHEST, "Maquina",
                        "/images/Pecho/Peck Deck Maquina.png"));
                list.add(new PredefinedExercise(null, "Flexiones (Push-ups)", MuscleGroup.CHEST, "Peso Corporal",
                        "/images/Pecho/Flexiones.png"));
                list.add(new PredefinedExercise(null, "Press en Maquina Convergente", MuscleGroup.CHEST, "Maquina",
                        "/images/Pecho/Press Maquina Convergente.png"));

                // ESPALDA
                list.add(new PredefinedExercise(null, "Dominadas", MuscleGroup.BACK, "Peso Corporal",
                        "/images/Espalda/Dominadas.png"));
                list.add(new PredefinedExercise(null, "Jalon al Pecho", MuscleGroup.BACK, "Polea",
                        "/images/Espalda/Jalon al pecho.png"));
                list.add(new PredefinedExercise(null, "Remo con Barra", MuscleGroup.BACK, "Barra",
                        "/images/Espalda/Remo con Barra.png"));
                list.add(new PredefinedExercise(null, "Remo en Punta", MuscleGroup.BACK, "Barra T",
                        "/images/Espalda/Remo en Punta.png"));
                list.add(new PredefinedExercise(null, "Remo con Mancuerna a una mano", MuscleGroup.BACK, "Mancuerna",
                        "/images/Espalda/Remo con Mancuernas a una mano.png"));
                list.add(new PredefinedExercise(null, "Remo Gironda", MuscleGroup.BACK, "Polea",
                        "/images/Espalda/Remo Gironda.png"));
                list.add(new PredefinedExercise(null, "Pull-over", MuscleGroup.BACK, "Mancuerna",
                        "/images/Espalda/Pull-Over.png"));
                list.add(new PredefinedExercise(null, "Peso Muerto", MuscleGroup.BACK, "Barra",
                        "/images/Espalda/Peso Muerto.png"));

                // PIERNAS
                list.add(new PredefinedExercise(null, "Sentadilla Libre", MuscleGroup.LEGS, "Barra",
                        "/images/Piernas/Sentadilla Libre.png"));
                list.add(new PredefinedExercise(null, "Sentadilla Hack", MuscleGroup.LEGS, "Maquina",
                        "/images/Piernas/Sentadilla Hack.png"));
                list.add(new PredefinedExercise(null, "Prensa Inclinada", MuscleGroup.LEGS, "Maquina",
                        "/images/Piernas/Prensa Inclinado.png"));
                list.add(new PredefinedExercise(null, "Zancadas (Lunges)", MuscleGroup.LEGS, "Mancuernas",
                        "/images/Piernas/Zancadas.png"));
                list.add(new PredefinedExercise(null, "Peso Muerto Rumano", MuscleGroup.LEGS, "Barra",
                        "/images/Piernas/Peso Muerto Rumano.png"));
                list.add(new PredefinedExercise(null, "Extensiones de Cuadriceps", MuscleGroup.LEGS, "Maquina",
                        "/images/Piernas/Extensiones de Cuadriceps.png"));
                list.add(new PredefinedExercise(null, "Curl Femoral Tumbado", MuscleGroup.LEGS, "Maquina",
                        "/images/Piernas/Curl Femoral Tumbado.png"));
                list.add(new PredefinedExercise(null, "Curl Femoral Sentado", MuscleGroup.LEGS, "Maquina",
                        "/images/Piernas/Curl Femoral Sentado.png"));
                list.add(new PredefinedExercise(null, "Elevacion de Gemelos de Pie", MuscleGroup.LEGS, "Maquina",
                        "/images/Piernas/Elevacion de Gemelos de Pie.png"));
                list.add(new PredefinedExercise(null, "Elevacion de Gemelos Sentado", MuscleGroup.LEGS, "Maquina",
                        "/images/Piernas/Elevacion de Gemelos Sentado.png"));
                list.add(new PredefinedExercise(null, "Hip Thrust", MuscleGroup.LEGS, "Barra",
                        "/images/Piernas/Hip Trust.png"));
                list.add(new PredefinedExercise(null, "Abductores", MuscleGroup.LEGS, "Maquina",
                        "/images/Piernas/Abductores.png"));
                list.add(new PredefinedExercise(null, "Aductores", MuscleGroup.LEGS, "Maquina",
                        "/images/Piernas/Aductores.png"));

                // HOMBROS
                list.add(new PredefinedExercise(null, "Press Militar", MuscleGroup.SHOULDERS, "Barra",
                        "/images/Hombros/Press Militar.png"));
                list.add(new PredefinedExercise(null, "Press Arnold", MuscleGroup.SHOULDERS, "Mancuernas",
                        "/images/Hombros/Press Arnold.png"));
                list.add(new PredefinedExercise(null, "Elevaciones Laterales", MuscleGroup.SHOULDERS, "Mancuernas",
                        "/images/Hombros/Elevaciones Laterales.png"));
                list.add(new PredefinedExercise(null, "Elevaciones Frontales", MuscleGroup.SHOULDERS, "Mancuernas",
                        "/images/Hombros/Elevaciones Frontales.png"));
                list.add(new PredefinedExercise(null, "Pajaros (Elevaciones Posteriores)", MuscleGroup.SHOULDERS,
                        "Mancuernas", "/images/Hombros/Pájaros.png"));
                list.add(new PredefinedExercise(null, "Face Pull", MuscleGroup.SHOULDERS, "Polea",
                        "/images/Hombros/Face Pull.png"));
                list.add(new PredefinedExercise(null, "Encogimientos para Trapecios", MuscleGroup.SHOULDERS,
                        "Mancuernas", "/images/Hombros/Encogimiento para trapecios.png"));

                // BRAZOS (Biceps / Triceps)
                list.add(new PredefinedExercise(null, "Curl de Biceps con Barra", MuscleGroup.ARMS, "Barra",
                        "/images/Brazos/Curl de biceps con barra.png"));
                list.add(new PredefinedExercise(null, "Curl Alterno", MuscleGroup.ARMS, "Mancuernas",
                        "/images/Brazos/Curl Alterno.png"));
                list.add(new PredefinedExercise(null, "Curl Martillo", MuscleGroup.ARMS, "Mancuernas",
                        "/images/Brazos/Curl Martillo.png"));
                list.add(new PredefinedExercise(null, "Curl Predicador", MuscleGroup.ARMS, "Barra Z",
                        "/images/Brazos/Curl Predicador.png"));
                list.add(new PredefinedExercise(null, "Curl en Polea Baja", MuscleGroup.ARMS, "Polea",
                        "/images/Brazos/Curl en polea Baja.png"));
                list.add(new PredefinedExercise(null, "Extension de Triceps en Polea", MuscleGroup.ARMS, "Polea",
                        "/images/Brazos/Extension de triceps en polea.png"));
                list.add(new PredefinedExercise(null, "Press Frances", MuscleGroup.ARMS, "Barra Z",
                        "/images/Brazos/Press Frances.png"));
                list.add(new PredefinedExercise(null, "Fondos entre Bancos", MuscleGroup.ARMS, "Peso Corporal",
                        "/images/Brazos/Fondos Entre Bancos.png"));
                list.add(new PredefinedExercise(null, "Patada de Triceps", MuscleGroup.ARMS, "Mancuerna",
                        "/images/Brazos/Patada de triceps.png"));
                list.add(new PredefinedExercise(null, "Extension Tras Nuca", MuscleGroup.ARMS, "Mancuerna",
                        "/images/Brazos/Extentsion tras nuca.png"));

                // CORE
                list.add(new PredefinedExercise(null, "Crunch Abdominal", MuscleGroup.CORE, "Peso Corporal",
                        "/images/Core/Crunch Abdominal.png"));
                list.add(new PredefinedExercise(null, "Plancha (Plank)", MuscleGroup.CORE, "Peso Corporal",
                        "/images/Core/Plancha.png"));
                list.add(new PredefinedExercise(null, "Elevacion de Piernas", MuscleGroup.CORE, "Peso Corporal",
                        "/images/Core/Elevacion de Piernas.png"));
                list.add(new PredefinedExercise(null, "Rueda Abdominal", MuscleGroup.CORE, "Rueda",
                        "/images/Core/Rueda Abdominal.png"));
                list.add(new PredefinedExercise(null, "Twist Ruso", MuscleGroup.CORE, "Disco/Balon",
                        "/images/Core/Twist Ruso.png"));
                list.add(new PredefinedExercise(null, "Crunch en Polea Alta", MuscleGroup.CORE, "Polea",
                        "/images/Core/Crunch en polea alta.png"));

                // CARDIO
                list.add(new PredefinedExercise(null, "Cinta de Correr", MuscleGroup.CARDIO, "Maquina",
                        "/images/Cardio/Cinta de Correr.png"));
                list.add(new PredefinedExercise(null, "Bicicleta Estatica", MuscleGroup.CARDIO, "Maquina",
                        "/images/Cardio/Bicicleta estática.png"));
                list.add(new PredefinedExercise(null, "Elíptica", MuscleGroup.CARDIO, "Maquina",
                        "/images/Cardio/Eliptica.png"));
                list.add(new PredefinedExercise(null, "Remo Ergometro", MuscleGroup.CARDIO, "Maquina",
                        "/images/Cardio/Remo Ergometro.png"));
                list.add(new PredefinedExercise(null, "Saltar a la Comba", MuscleGroup.CARDIO, "Comba",
                        "/images/Cardio/Saltar a la comba.png"));
                list.add(new PredefinedExercise(null, "Burpees", MuscleGroup.CARDIO, "Peso Corporal",
                        "/images/Cardio/Burpees.png"));

                // FULL BODY / CROSSFIT
                list.add(new PredefinedExercise(null, "Kettlebell Swing", MuscleGroup.FULL_BODY, "Pesa Rusa",
                        "/images/Full Body/Kettlebell Swing.png"));
                list.add(new PredefinedExercise(null, "Clean and Jerk", MuscleGroup.FULL_BODY, "Barra",
                        "/images/Full Body/Clean and Jerk.png"));
                list.add(new PredefinedExercise(null, "Snatch", MuscleGroup.FULL_BODY, "Barra",
                        "/images/Full Body/Snatch.png"));

                repository.saveAll(list);
                log.info("Catalogo de ejercicios guardado. ({})", list.size());
            } else {
                log.info(
                        "Actualizando rutas de imagenes en catalogo existente a partir de los nombres de la base de datos...");
                List<PredefinedExercise> list = repository.findAll();
                boolean updated = false;
                for (PredefinedExercise pe : list) {
                    // Si el nombre en la BD es "Press de Banca Plato", lo corregimos en BD a "Press
                    // de Banca Plano"
                    if ("Press de Banca Plato".equalsIgnoreCase(pe.getName())) {
                        pe.setName("Press de Banca Plano");
                        updated = true;
                    }

                    String path = getImagePath(pe);
                    if (path != null && (pe.getImageUrl() == null || !pe.getImageUrl().equals(path))) {
                        pe.setImageUrl(path);
                        updated = true;
                    }
                }
                if (updated) {
                    repository.saveAll(list);
                    log.info("Rutas de imagenes actualizadas en el catalogo.");
                }

                // Sincronizar ejercicios asignados existentes para que tengan su
                // predefined_exercise_id
                log.info("Sincronizando ejercicios de clientes con el catálogo global...");
                List<Exercise> exercises = exerciseRepository.findAll();
                boolean exercisesUpdated = false;
                int matchedCount = 0;
                int unmatchedCount = 0;
                for (Exercise ex : exercises) {
                    if (ex.getPredefinedExercise() == null) {
                        String normalizedName = normalize(ex.getName());
                        PredefinedExercise matchedPe = null;
                        for (PredefinedExercise pe : list) {
                            String normPeName = normalize(pe.getName());
                            // Coincidencia exacta o alias comunes
                            if (normPeName.equals(normalizedName)
                                    || (normalizedName.equals("press banca")
                                            && normPeName.equals("press de banca plano"))
                                    || (normalizedName.equals("press de banca")
                                            && normPeName.equals("press de banca plano"))
                                    || (normalizedName.equals("remo") && normPeName.equals("remo con barra"))
                                    || (normalizedName.equals("sentadilla") && normPeName.equals("sentadilla libre"))
                                    || (normalizedName.equals("prensa") && normPeName.equals("prensa inclinada"))) {
                                matchedPe = pe;
                                break;
                            }
                        }

                        // Si aún no hace match, intentamos por subcadena
                        if (matchedPe == null) {
                            for (PredefinedExercise pe : list) {
                                String normPeName = normalize(pe.getName());
                                if (normPeName.contains(normalizedName) || normalizedName.contains(normPeName)) {
                                    matchedPe = pe;
                                    break;
                                }
                            }
                        }

                        if (matchedPe != null) {
                            ex.setPredefinedExercise(matchedPe);
                            exercisesUpdated = true;
                            matchedCount++;
                        } else {
                            log.warn("⚠️ No se pudo emparejar el ejercicio asignado: '{}'", ex.getName());
                            unmatchedCount++;
                        }
                    }
                }
                if (exercisesUpdated) {
                    exerciseRepository.saveAll(exercises);
                    log.info(
                            "Ejercicios de clientes sincronizados con el catálogo global. Emparejados: {}, No emparejados: {}",
                            matchedCount, unmatchedCount);
                } else {
                    log.info("No se requirieron actualizaciones de sincronización de ejercicios de clientes.");
                }

                // BORRADO COMPLETO DE DIETAS Y ENTRENAMIENTOS PARA RECARGA LIMPIA
                log.info(
                        "🗑️ Borrando todas las dietas y entrenamientos existentes para hacer una recarga limpia y premium...");
                workoutPlanRepository.deleteAll();
                dietRepository.deleteAll();
                log.info("✅ Base de datos limpia de dietas y entrenamientos antiguos.");

                log.info("ℹ️ Generando 2 rutinas y 2 dietas profesionales premium para cada cliente registrado...");
                List<Client> clients = clientRepository.findAll();
                List<PredefinedExercise> predefinedCatalog = repository.findAll();

                if (!clients.isEmpty() && !predefinedCatalog.isEmpty()) {
                    // Obtener un entrenador por defecto
                    Trainer defaultTrainer = null;
                    List<Trainer> trainersList = trainerRepository.findAll();
                    if (!trainersList.isEmpty()) {
                        defaultTrainer = trainersList.get(0);
                    }

                    int plansAdded = 0;
                    int dietsAdded = 0;

                    for (Client client : clients) {
                        Trainer trainer = client.getTrainer() != null ? client.getTrainer() : defaultTrainer;
                        if (trainer == null)
                            continue;

                        // ==========================================
                        // A. PLAN DE ENTRENAMIENTO 1: HIPERTROFIA Y ACONDICIONAMIENTO (5 DÍAS)
                        // ==========================================
                        {
                            WorkoutPlan wp = new WorkoutPlan();
                            wp.setClient(client);
                            wp.setTrainer(trainer);
                            wp.setTitle("Plan 1: Hipertrofia y Acondicionamiento (5 Días)");
                            wp.setObjective("Aumento de Masa Muscular y Resistencia Física");
                            wp.setNotes(
                                    "Rutina integral diseñada por el Entrenador. Enfocada en volumen y desarrollo muscular. Hidrátate bien, controla la técnica de cada ejercicio y mantén un calentamiento previo de 5-10 minutos.");
                            wp.setStartDate(LocalDate.now());
                            wp.setEndDate(LocalDate.now().plusMonths(3));
                            wp.setActive(true);

                            List<WorkoutDay> daysList = new ArrayList<>();
                            daysList.add(new WorkoutDay(null, wp, DayOfWeekPlan.MONDAY,
                                    "Pectoral & Core - Empuje y Estabilidad",
                                    "Enfócate en mantener el core activo y controlar el descenso en todos los press. Rango completo de movimiento."));
                            daysList.add(new WorkoutDay(null, wp, DayOfWeekPlan.TUESDAY,
                                    "Espalda & Cardio - Tracción y Resistencia",
                                    "Mantén la espalda recta en los remos y no balancees el torso en los jalones. Aprieta las escápulas."));
                            daysList.add(new WorkoutDay(null, wp, DayOfWeekPlan.WEDNESDAY,
                                    "Piernas Completas - Tren Inferior",
                                    "Baja de forma controlada en las sentadillas e hip thrust. Aprieta el glúteo y mantén el peso en los talones."));
                            daysList.add(new WorkoutDay(null, wp, DayOfWeekPlan.THURSDAY,
                                    "Hombros & Core - Deltoides y Estabilidad",
                                    "Mantén una ligera flexión de codo en las elevaciones y no balancees el cuerpo. Deltoides redondos y core firme."));
                            daysList.add(new WorkoutDay(null, wp, DayOfWeekPlan.FRIDAY,
                                    "Brazos & Full Body - Aislamiento y Resistencia",
                                    "Mantén los codos fijos al torso en el curl de bíceps y la extensión de tríceps. Gran bombeo de brazos."));

                            wp.setWorkoutDays(daysList);
                            WorkoutPlan savedWp = workoutPlanRepository.save(wp);

                            // Ejercicios del Plan 1 (Los 62 ejercicios del catálogo distribuidos
                            // lógicamente)
                            String[] mondayEx = {
                                    "Press de Banca Plano", "Press Inclinado", "Aperturas", "Cruces en Polea",
                                    "Fondos en Paralelas", "Press Declinado", "Peck Deck", "Flexiones (Push-ups)",
                                    "Press en Maquina Convergente", "Crunch Abdominal", "Plancha (Plank)",
                                    "Elevación de Piernas", "Rueda Abdominal", "Twist Ruso", "Crunch en Polea Alta"
                            };

                            String[] tuesdayEx = {
                                    "Dominadas", "Jalón al Pecho", "Remo con Barra", "Remo en Punta",
                                    "Remo con Mancuerna a una mano", "Remo Gironda", "Pull-over", "Peso Muerto",
                                    "Cinta de Correr", "Bicicleta Estática", "Elíptica", "Remo Ergómetro",
                                    "Saltar a la Comba", "Burpees"
                            };

                            String[] wednesdayEx = {
                                    "Sentadilla Libre", "Sentadilla Hack", "Prensa Inclinada", "Zancadas (Lunges)",
                                    "Peso Muerto Rumano", "Extensiones de Cuádriceps", "Curl Femoral Tumbado",
                                    "Curl Femoral Sentado", "Elevación de Gemelos de Pie",
                                    "Elevación de Gemelos Sentado",
                                    "Hip Thrust", "Abductores", "Aductores"
                            };

                            String[] thursdayEx = {
                                    "Press Militar", "Press Arnold", "Elevaciones Laterales", "Elevaciones Frontales",
                                    "Pájaros (Elevaciones Posteriores)", "Face Pull", "Encogimientos para Trapecios",
                                    "Plancha (Plank)", "Elevación de Piernas", "Crunch Abdominal"
                            };

                            String[] fridayEx = {
                                    "Curl de Bíceps con Barra", "Curl Alterno", "Curl Martillo", "Curl Predicador",
                                    "Curl en Polea Baja", "Extensión de Tríceps en Polea", "Press Francés",
                                    "Fondos entre Bancos", "Patada de Tríceps", "Extensión Tras Nuca",
                                    "Kettlebell Swing", "Clean and Jerk", "Snatch"
                            };

                            for (WorkoutDay wd : savedWp.getWorkoutDays()) {
                                String[] names = null;
                                switch (wd.getDayOfWeek()) {
                                    case MONDAY:
                                        names = mondayEx;
                                        break;
                                    case TUESDAY:
                                        names = tuesdayEx;
                                        break;
                                    case WEDNESDAY:
                                        names = wednesdayEx;
                                        break;
                                    case THURSDAY:
                                        names = thursdayEx;
                                        break;
                                    case FRIDAY:
                                        names = fridayEx;
                                        break;
                                    default:
                                        break;
                                }

                                if (names != null) {
                                    List<Exercise> exercisesList = new ArrayList<>();
                                    for (String name : names) {
                                        PredefinedExercise peMatched = null;
                                        for (PredefinedExercise pe : predefinedCatalog) {
                                            if (pe.getName().equalsIgnoreCase(name)) {
                                                peMatched = pe;
                                                break;
                                            }
                                        }

                                        if (peMatched != null) {
                                            Exercise ex = new Exercise();
                                            ex.setWorkoutDay(wd);
                                            ex.setName(peMatched.getName());
                                            ex.setPredefinedExercise(peMatched);
                                            ex.setImageUrl(peMatched.getImageUrl());

                                            String group = peMatched.getMuscleGroup().name();
                                            if (group.equals("CORE")) {
                                                ex.setSets(3);
                                                ex.setReps(peMatched.getName().contains("Plancha") ? "60 segundos"
                                                        : "15-20");
                                                ex.setRestSeconds(45);
                                                ex.setDurationMinutes(5);
                                            } else if (group.equals("CARDIO")) {
                                                ex.setSets(1);
                                                ex.setReps("1 serie");
                                                ex.setRestSeconds(0);
                                                ex.setDurationMinutes(20);
                                            } else if (group.equals("FULL_BODY")) {
                                                ex.setSets(3);
                                                ex.setReps("8-10");
                                                ex.setRestSeconds(90);
                                                ex.setDurationMinutes(8);
                                            } else {
                                                ex.setSets(4);
                                                ex.setReps("10-12");
                                                ex.setRestSeconds(90);
                                                ex.setDurationMinutes(10);
                                            }
                                            ex.setNotes("Mantén la técnica estricta y controla la bajada del peso.");
                                            exercisesList.add(ex);
                                        }
                                    }
                                    exerciseRepository.saveAll(exercisesList);
                                }
                            }
                            plansAdded++;
                        }

                        // ==========================================
                        // A2. PLAN DE ENTRENAMIENTO 2: FUERZA Y TONIFICACIÓN (3 DÍAS)
                        // ==========================================
                        {
                            WorkoutPlan wp2 = new WorkoutPlan();
                            wp2.setClient(client);
                            wp2.setTrainer(trainer);
                            wp2.setTitle("Plan 2: Fuerza y Tonificación (3 Días - Full Body)");
                            wp2.setObjective("Desarrollo de Fuerza Funcional y Definición Muscular");
                            wp2.setNotes(
                                    "Rutina de cuerpo completo (Full Body) optimizada para 3 días a la semana. Ideal para maximizar el tiempo y favorecer la recuperación del sistema nervioso.");
                            wp2.setStartDate(LocalDate.now());
                            wp2.setEndDate(LocalDate.now().plusMonths(3));
                            wp2.setActive(false); // Segundo plan inactivo por defecto, listo para activar

                            List<WorkoutDay> daysList2 = new ArrayList<>();
                            daysList2.add(new WorkoutDay(null, wp2, DayOfWeekPlan.MONDAY,
                                    "Full Body A - Fuerza y Potencia",
                                    "Enfócate en ejercicios multiarticulares grandes. Levanta cargas que te exijan mantener una buena técnica."));
                            daysList2.add(new WorkoutDay(null, wp2, DayOfWeekPlan.WEDNESDAY,
                                    "Full Body B - Resistencia y Core",
                                    "Tiempos de descanso controlados, busca mantener alta la frecuencia cardíaca y fatiga muscular acumulada."));
                            daysList2.add(new WorkoutDay(null, wp2, DayOfWeekPlan.FRIDAY,
                                    "Cardio & Acondicionamiento Metabólico",
                                    "Día enfocado en resistencia cardiovascular, agilidad, potencia metabólica y quema calórica."));

                            wp2.setWorkoutDays(daysList2);
                            WorkoutPlan savedWp2 = workoutPlanRepository.save(wp2);

                            String[] monEx2 = { "Press de Banca Plano", "Sentadilla Libre", "Remo con Barra",
                                    "Press Militar", "Curl de Bíceps con Barra", "Plancha (Plank)" };
                            String[] wedEx2 = { "Dominadas", "Prensa Inclinada", "Press Inclinado",
                                    "Elevaciones Laterales", "Extensión de Tríceps en Polea", "Twist Ruso" };
                            String[] friEx2 = { "Cinta de Correr", "Kettlebell Swing", "Burpees", "Elíptica",
                                    "Crunch Abdominal" };

                            for (WorkoutDay wd : savedWp2.getWorkoutDays()) {
                                String[] names = null;
                                switch (wd.getDayOfWeek()) {
                                    case MONDAY:
                                        names = monEx2;
                                        break;
                                    case WEDNESDAY:
                                        names = wedEx2;
                                        break;
                                    case FRIDAY:
                                        names = friEx2;
                                        break;
                                    default:
                                        break;
                                }

                                if (names != null) {
                                    List<Exercise> exercisesList = new ArrayList<>();
                                    for (String name : names) {
                                        PredefinedExercise peMatched = null;
                                        for (PredefinedExercise pe : predefinedCatalog) {
                                            if (pe.getName().equalsIgnoreCase(name)) {
                                                peMatched = pe;
                                                break;
                                            }
                                        }

                                        if (peMatched != null) {
                                            Exercise ex = new Exercise();
                                            ex.setWorkoutDay(wd);
                                            ex.setName(peMatched.getName());
                                            ex.setPredefinedExercise(peMatched);
                                            ex.setImageUrl(peMatched.getImageUrl());

                                            String group = peMatched.getMuscleGroup().name();
                                            if (group.equals("CORE")) {
                                                ex.setSets(3);
                                                ex.setReps(peMatched.getName().contains("Plancha") ? "60 segundos"
                                                        : "15-20");
                                                ex.setRestSeconds(45);
                                                ex.setDurationMinutes(5);
                                            } else if (group.equals("CARDIO")) {
                                                ex.setSets(1);
                                                ex.setReps("1 serie");
                                                ex.setRestSeconds(0);
                                                ex.setDurationMinutes(20);
                                            } else if (group.equals("FULL_BODY")) {
                                                ex.setSets(3);
                                                ex.setReps("12-15");
                                                ex.setRestSeconds(60);
                                                ex.setDurationMinutes(8);
                                            } else {
                                                ex.setSets(4);
                                                ex.setReps("8-10");
                                                ex.setRestSeconds(90);
                                                ex.setDurationMinutes(10);
                                            }
                                            ex.setNotes(
                                                    "Movimiento explosivo en la fase concéntrica y controlado en la excéntrica.");
                                            exercisesList.add(ex);
                                        }
                                    }
                                    exerciseRepository.saveAll(exercisesList);
                                }
                            }
                            plansAdded++;
                        }

                        // ==========================================
                        // B. PLAN NUTRICIONAL 1: VOLUMEN LIMPIO - HIPERTROFIA
                        // ==========================================
                        {
                            Diet diet1 = new Diet();
                            diet1.setClient(client);
                            diet1.setTrainer(trainer);
                            diet1.setTitle("Plan 1: Volumen Limpio y Ganancia de Fuerza");
                            diet1.setDescription(
                                    "Dieta hipercalórica rica en proteínas y carbohidratos complejos, diseñada para optimizar el rendimiento de entrenamiento y favorecer la ganancia muscular magra.");
                            diet1.setStartDate(LocalDate.now());
                            diet1.setEndDate(LocalDate.now().plusMonths(3));
                            diet1.setActive(true);

                            Diet savedDiet1 = dietRepository.save(diet1);
                            List<DietMeal> mealsList1 = new ArrayList<>();

                            // Desayuno
                            DietMeal breakfast = new DietMeal(null, savedDiet1, "DESAYUNO", "08:00",
                                    """
                                    [
                                      {
                                        "name": "Opción A: Tortilla y Avena",
                                        "items": [
                                          {"name": "Claras de huevo", "grams": "3 uds", "kcal": "50", "notes": "En tortilla"},
                                          {"name": "Huevo entero", "grams": "1 ud", "kcal": "70", "notes": "En tortilla"},
                                          {"name": "Avena en copos", "grams": "60g", "kcal": "220", "notes": "Cocida"},
                                          {"name": "Plátano", "grams": "1 ud", "kcal": "110", "notes": ""},
                                          {"name": "Café solo", "grams": "1 taza", "kcal": "0", "notes": ""}
                                        ]
                                      },
                                      {
                                        "name": "Opción B: Batido de Proteínas",
                                        "items": [
                                          {"name": "Proteína de suero (Whey)", "grams": "30g", "kcal": "120", "notes": ""},
                                          {"name": "Plátano", "grams": "1 ud", "kcal": "110", "notes": ""},
                                          {"name": "Mantequilla de cacahuete", "grams": "30g", "kcal": "180", "notes": ""},
                                          {"name": "Leche de almendras", "grams": "250ml", "kcal": "40", "notes": ""}
                                        ]
                                      }
                                    ]
                                    """,
                                    450, "Aporte energético inicial para activar la síntesis proteica.");
                            breakfast.setProtein(30.0);
                            breakfast.setCarbs(45.0);
                            breakfast.setFats(15.0);
                            breakfast.setCompleted(false);
                            mealsList1.add(breakfast);

                            // Almuerzo
                            DietMeal snack1 = new DietMeal(null, savedDiet1, "ALMUERZO", "11:30",
                                    """
                                    [
                                      {
                                        "name": "Opción A: Queso Batido con Nueces",
                                        "items": [
                                          {"name": "Queso batido 0%", "grams": "150g", "kcal": "80", "notes": ""},
                                          {"name": "Nueces", "grams": "20g", "kcal": "130", "notes": "Un puñado"},
                                          {"name": "Manzana", "grams": "1 ud", "kcal": "70", "notes": ""}
                                        ]
                                      },
                                      {
                                        "name": "Opción B: Tostada de Pavo",
                                        "items": [
                                          {"name": "Pan integral", "grams": "1 rebanada", "kcal": "80", "notes": "Tostado"},
                                          {"name": "Pechuga de pavo", "grams": "80g", "kcal": "70", "notes": "Bajo en sal"},
                                          {"name": "Tomate", "grams": "50g", "kcal": "10", "notes": "En rodajas"}
                                        ]
                                      }
                                    ]
                                    """,
                                    250, "Tentempié ligero para mantener niveles estables de nitrógeno.");
                            snack1.setProtein(20.0);
                            snack1.setCarbs(25.0);
                            snack1.setFats(8.0);
                            snack1.setCompleted(false);
                            mealsList1.add(snack1);

                            // Comida
                            DietMeal lunch = new DietMeal(null, savedDiet1, "COMIDA", "14:30",
                                    """
                                    [
                                      {
                                        "name": "Opción A: Pollo con Arroz y Brócoli",
                                        "items": [
                                          {"name": "Pechuga de pollo", "grams": "150g", "kcal": "250", "notes": "A la plancha"},
                                          {"name": "Arroz integral", "grams": "80g", "kcal": "280", "notes": "En seco"},
                                          {"name": "Brócoli", "grams": "200g", "kcal": "70", "notes": "Al vapor"}
                                        ]
                                      },
                                      {
                                        "name": "Opción B: Salmón con Patata",
                                        "items": [
                                          {"name": "Lomo de salmón", "grams": "150g", "kcal": "300", "notes": "Al horno"},
                                          {"name": "Patata", "grams": "200g", "kcal": "170", "notes": "Al vapor"},
                                          {"name": "Ensalada de espinacas y tomate", "grams": "150g", "kcal": "50", "notes": ""}
                                        ]
                                      }
                                    ]
                                    """,
                                    650, "Comida densa nutricionalmente, rica en micronutrientes y aminoácidos.");
                            lunch.setProtein(45.0);
                            lunch.setCarbs(60.0);
                            lunch.setFats(18.0);
                            lunch.setCompleted(false);
                            mealsList1.add(lunch);

                            // Merienda
                            DietMeal snack2 = new DietMeal(null, savedDiet1, "MERIENDA", "18:00",
                                    """
                                    [
                                      {
                                        "name": "Opción A: Atún con Tortitas y Manzana",
                                        "items": [
                                          {"name": "Atún al natural", "grams": "1 lata", "kcal": "80", "notes": "Escurrido"},
                                          {"name": "Tortitas de arroz", "grams": "2 uds", "kcal": "60", "notes": ""},
                                          {"name": "Manzana mediana", "grams": "1 ud", "kcal": "75", "notes": ""}
                                        ]
                                      },
                                      {
                                        "name": "Opción B: Yogur con Almendras",
                                        "items": [
                                          {"name": "Yogur griego ligero", "grams": "1 ud", "kcal": "90", "notes": ""},
                                          {"name": "Almendras", "grams": "20g", "kcal": "120", "notes": "Al natural"},
                                          {"name": "Frutos rojos", "grams": "50g", "kcal": "30", "notes": "Arándanos o fresas"}
                                        ]
                                      }
                                    ]
                                    """,
                                    250, "Aporte óptimo para prepararte de cara al entrenamiento.");
                            snack2.setProtein(22.0);
                            snack2.setCarbs(20.0);
                            snack2.setFats(10.0);
                            snack2.setCompleted(false);
                            mealsList1.add(snack2);

                            // Cena
                            DietMeal dinner = new DietMeal(null, savedDiet1, "CENA", "21:30",
                                    """
                                    [
                                      {
                                        "name": "Opción A: Pescado con Puré",
                                        "items": [
                                          {"name": "Merluza o lenguado", "grams": "180g", "kcal": "160", "notes": "A la plancha"},
                                          {"name": "Puré de calabacín", "grams": "200g", "kcal": "90", "notes": ""},
                                          {"name": "Espárragos trigueros", "grams": "100g", "kcal": "40", "notes": "A la plancha"}
                                        ]
                                      },
                                      {
                                        "name": "Opción B: Ternera con Ensalada",
                                        "items": [
                                          {"name": "Ternera magra", "grams": "150g", "kcal": "220", "notes": "A la plancha"},
                                          {"name": "Ensalada de espinacas", "grams": "100g", "kcal": "25", "notes": ""},
                                          {"name": "Tomate cherry", "grams": "50g", "kcal": "15", "notes": ""},
                                          {"name": "Aguacate", "grams": "0.5 ud", "kcal": "120", "notes": ""}
                                        ]
                                      }
                                    ]
                                    """,
                                    400, "Cena de asimilación lenta para nutrir los músculos durante el descanso.");
                            dinner.setProtein(40.0);
                            dinner.setCarbs(15.0);
                            dinner.setFats(12.0);
                            dinner.setCompleted(false);
                            mealsList1.add(dinner);

                            savedDiet1.setMeals(mealsList1);
                            dietRepository.save(savedDiet1);
                            dietsAdded++;
                        }

                        // ==========================================
                        // B2. PLAN NUTRICIONAL 2: DEFINICIÓN Y PÉRDIDA DE GRASA
                        // ==========================================
                        {
                            Diet diet2 = new Diet();
                            diet2.setClient(client);
                            diet2.setTrainer(trainer);
                            diet2.setTitle("Plan 2: Definición Estricta y Tonificación");
                            diet2.setDescription(
                                    "Dieta hipocalórica y alta en proteínas diseñada para reducir el porcentaje de grasa corporal manteniendo intacta la masa muscular y favoreciendo la definición.");
                            diet2.setStartDate(LocalDate.now());
                            diet2.setEndDate(LocalDate.now().plusMonths(3));
                            diet2.setActive(false); // Segunda dieta inactiva por defecto, lista para alternar

                            Diet savedDiet2 = dietRepository.save(diet2);
                            List<DietMeal> mealsList2 = new ArrayList<>();

                            // Desayuno
                            DietMeal breakfast = new DietMeal(null, savedDiet2, "DESAYUNO", "08:00",
                                    """
                                    [
                                      {
                                        "name": "Opción A: Claras y Avena",
                                        "items": [
                                          {"name": "Claras de huevo", "grams": "4 uds", "kcal": "65", "notes": "En tortilla"},
                                          {"name": "Avena", "grams": "40g", "kcal": "150", "notes": "Cocida con agua y canela"},
                                          {"name": "Café solo", "grams": "1 taza", "kcal": "0", "notes": ""}
                                        ]
                                      },
                                      {
                                        "name": "Opción B: Queso Fresco con Nueces",
                                        "items": [
                                          {"name": "Queso fresco batido 0%", "grams": "200g", "kcal": "100", "notes": ""},
                                          {"name": "Nueces", "grams": "15g", "kcal": "100", "notes": ""},
                                          {"name": "Arándanos", "grams": "80g", "kcal": "40", "notes": "Un tazón pequeño"}
                                        ]
                                      }
                                    ]
                                    """,
                                    320, "Comida baja en calorías para estabilizar la insulina matutina.");
                            breakfast.setProtein(28.0);
                            breakfast.setCarbs(30.0);
                            breakfast.setFats(8.0);
                            breakfast.setCompleted(false);
                            mealsList2.add(breakfast);

                            // Almuerzo
                            DietMeal snack1 = new DietMeal(null, savedDiet2, "ALMUERZO", "11:30",
                                    """
                                    [
                                      {
                                        "name": "Opción A: Manzana y Atún",
                                        "items": [
                                          {"name": "Manzana mediana", "grams": "1 ud", "kcal": "75", "notes": ""},
                                          {"name": "Atún al natural", "grams": "1 lata", "kcal": "80", "notes": "Escurrida"}
                                        ]
                                      },
                                      {
                                        "name": "Opción B: Tortitas con Pavo",
                                        "items": [
                                          {"name": "Tortitas de arroz integral", "grams": "2 uds", "kcal": "60", "notes": ""},
                                          {"name": "Pechuga de pavo", "grams": "60g", "kcal": "55", "notes": "Baja en sal"}
                                        ]
                                      }
                                    ]
                                    """,
                                    150, "Snack saciante de bajas calorías.");
                            snack1.setProtein(15.0);
                            snack1.setCarbs(20.0);
                            snack1.setFats(2.0);
                            snack1.setCompleted(false);
                            mealsList2.add(snack1);

                            // Comida
                            DietMeal lunch = new DietMeal(null, savedDiet2, "COMIDA", "14:30",
                                    """
                                    [
                                      {
                                        "name": "Opción A: Pavo con Quinoa y Ensalada",
                                        "items": [
                                          {"name": "Pechuga de pavo", "grams": "150g", "kcal": "165", "notes": "A la plancha"},
                                          {"name": "Quinoa", "grams": "50g", "kcal": "185", "notes": "En seco"},
                                          {"name": "Ensalada verde", "grams": "1 plato", "kcal": "30", "notes": "Lechuga y pepino ilimitado"}
                                        ]
                                      },
                                      {
                                        "name": "Opción B: Merluza con Patata",
                                        "items": [
                                          {"name": "Filete de merluza", "grams": "150g", "kcal": "130", "notes": "Al horno"},
                                          {"name": "Patata", "grams": "150g", "kcal": "130", "notes": "Al vapor"},
                                          {"name": "Espárragos verdes", "grams": "100g", "kcal": "35", "notes": ""}
                                        ]
                                      }
                                    ]
                                    """,
                                    450, "Aporte equilibrado de proteínas y fibra para promover saciedad.");
                            lunch.setProtein(40.0);
                            lunch.setCarbs(45.0);
                            lunch.setFats(5.0);
                            lunch.setCompleted(false);
                            mealsList2.add(lunch);

                            // Merienda
                            DietMeal snack2 = new DietMeal(null, savedDiet2, "MERIENDA", "18:00",
                                    """
                                    [
                                      {
                                        "name": "Opción A: Batido de Proteína",
                                        "items": [
                                          {"name": "Proteína de suero (Whey)", "grams": "30g", "kcal": "120", "notes": "Disuelta en agua"},
                                          {"name": "Almendras", "grams": "10 uds", "kcal": "70", "notes": "Al natural"}
                                        ]
                                      },
                                      {
                                        "name": "Opción B: Yogur con Fresas",
                                        "items": [
                                          {"name": "Yogur griego ligero", "grams": "150g", "kcal": "110", "notes": ""},
                                          {"name": "Fresas", "grams": "80g", "kcal": "30", "notes": "Un puñado picadas"}
                                        ]
                                      }
                                    ]
                                    """,
                                    200, "Refuerzo proteico óptimo antes del descanso vespertino.");
                            snack2.setProtein(25.0);
                            snack2.setCarbs(10.0);
                            snack2.setFats(6.0);
                            snack2.setCompleted(false);
                            mealsList2.add(snack2);

                            // Cena
                            DietMeal dinner = new DietMeal(null, savedDiet2, "CENA", "21:30",
                                    """
                                    [
                                      {
                                        "name": "Opción A: Lenguado con Calabaza",
                                        "items": [
                                          {"name": "Lenguado", "grams": "200g", "kcal": "170", "notes": "A la plancha"},
                                          {"name": "Puré de calabaza", "grams": "150g", "kcal": "60", "notes": ""},
                                          {"name": "Ensalada verde", "grams": "1 plato", "kcal": "60", "notes": "Aliñada con 1 cdta de aceite"}
                                        ]
                                      },
                                      {
                                        "name": "Opción B: Pollo con Verduras",
                                        "items": [
                                          {"name": "Pechuga de pollo", "grams": "150g", "kcal": "200", "notes": "A la plancha"},
                                          {"name": "Salteado de calabacín", "grams": "100g", "kcal": "35", "notes": ""},
                                          {"name": "Champiñones al ajillo", "grams": "100g", "kcal": "45", "notes": ""}
                                        ]
                                      }
                                    ]
                                    """,
                                    350, "Cena ligera de digestión óptima y baja carga de carbohidratos.");
                            dinner.setProtein(38.0);
                            dinner.setCarbs(15.0);
                            dinner.setFats(8.0);
                            dinner.setCompleted(false);
                            mealsList2.add(dinner);

                            savedDiet2.setMeals(mealsList2);
                            dietRepository.save(savedDiet2);
                            dietsAdded++;
                        }
                    }

                    log.info(
                            "✅ Generación premium completada. Se crearon {} planes de entrenamiento y {} planes de dieta profesionales para tus clientes.",
                            plansAdded, dietsAdded);
                }
            }
        };
    }

    private String getImagePath(PredefinedExercise pe) {
        if (pe == null || pe.getName() == null || pe.getMuscleGroup() == null)
            return null;

        String folderName;
        switch (pe.getMuscleGroup()) {
            case CHEST:
                folderName = "Pecho";
                break;
            case BACK:
                folderName = "Espalda";
                break;
            case LEGS:
                folderName = "Piernas";
                break;
            case SHOULDERS:
                folderName = "Hombros";
                break;
            case ARMS:
                folderName = "Brazos";
                break;
            case CORE:
                folderName = "Core";
                break;
            case CARDIO:
                folderName = "Cardio";
                break;
            case FULL_BODY:
                folderName = "Full Body";
                break;
            default:
                return null;
        }

        return "/images/" + folderName + "/" + pe.getName() + ".png";
    }

    private String normalize(String name) {
        if (name == null)
            return "";
        return name.toLowerCase()
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u")
                .replace("ü", "u")
                .trim();
    }
}
