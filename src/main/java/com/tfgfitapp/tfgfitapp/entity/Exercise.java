package com.tfgfitapp.tfgfitapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 * Entidad que representa un Ejercicio individual dentro de un plan de entrenamiento.
 * 
 * Se asocia a un {@link WorkoutDay} y define los parámetros de ejecución:
 * series, repeticiones (puede ser un rango o texto), descanso y duración.
 */
@Entity
@Table(name = "exercises")
public class Exercise {

    public Exercise() {}

    public Exercise(Long id, WorkoutDay workoutDay, String name, Integer sets, String reps, Integer restSeconds, Integer durationMinutes, String notes) {
        this.id = id;
        this.workoutDay = workoutDay;
        this.name = name;
        this.sets = sets;
        this.reps = reps;
        this.restSeconds = restSeconds;
        this.durationMinutes = durationMinutes;
        this.notes = notes;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_day_id", nullable = false)
    @JsonIgnore
    private WorkoutDay workoutDay;

    @Column(nullable = false, length = 150)
    private String name;

    private Integer sets;

    @Column(length = 50)
    private String reps;

    @Column(name = "rest_seconds")
    private Integer restSeconds;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(length = 255)
    private String gifUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "predefined_exercise_id")
    private PredefinedExercise predefinedExercise;

    /** @return El identificador único del ejercicio. */
    public Long getId() { return id; }
    /** @param id El nuevo ID a asignar. */
    public void setId(Long id) { this.id = id; }
    /** @return El día de entrenamiento asociado. */
    public WorkoutDay getWorkoutDay() { return workoutDay; }
    /** @param workoutDay El día de entrenamiento a asignar. */
    public void setWorkoutDay(WorkoutDay workoutDay) { this.workoutDay = workoutDay; }
    /** @return Nombre del ejercicio. */
    public String getName() { return name; }
    /** @param name El nuevo nombre. */
    public void setName(String name) { this.name = name; }
    /** @return Número de series. */
    public Integer getSets() { return sets; }
    /** @param sets El nuevo número de series. */
    public void setSets(Integer sets) { this.sets = sets; }
    /** @return Repeticiones (e.g., "12" o "10-12"). */
    public String getReps() { return reps; }
    /** @param reps El nuevo valor de repeticiones. */
    public void setReps(String reps) { this.reps = reps; }
    /** @return Segundos de descanso entre series. */
    public Integer getRestSeconds() { return restSeconds; }
    /** @param restSeconds Los nuevos segundos de descanso. */
    public void setRestSeconds(Integer restSeconds) { this.restSeconds = restSeconds; }
    /** @return Duración estimada en minutos. */
    public Integer getDurationMinutes() { return durationMinutes; }
    /** @param durationMinutes La nueva duración. */
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    /** @return Notas sobre la ejecución del ejercicio. */
    public String getNotes() { return notes; }
    /** @param notes Las nuevas notas. */
    public void setNotes(String notes) { this.notes = notes; }
    /** @return URL de la animación GIF del ejercicio. */
    public String getGifUrl() { return gifUrl; }
    /** @param gifUrl La nueva URL del GIF. */
    public void setGifUrl(String gifUrl) { this.gifUrl = gifUrl; }
    /** @return El ejercicio predefinido base (si aplica). */
    public PredefinedExercise getPredefinedExercise() { return predefinedExercise; }
    /** @param predefinedExercise El ejercicio predefinido a asociar. */
    public void setPredefinedExercise(PredefinedExercise predefinedExercise) { this.predefinedExercise = predefinedExercise; }
}
