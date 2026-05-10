package com.tfgfitapp.tfgfitapp.controller;

import com.tfgfitapp.tfgfitapp.entity.PredefinedExercise;
import com.tfgfitapp.tfgfitapp.repository.PredefinedExerciseRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador para la consulta de ejercicios predefinidos.
 * 
 * Proporciona el catálogo base de ejercicios disponibles en el sistema,
 * permitiendo su filtrado o agrupación por categoría (grupo muscular).
 */
@RestController
@RequestMapping("/api/predefined-exercises")
public class PredefinedExerciseController {

    public PredefinedExerciseController(PredefinedExerciseRepository repository) {
        this.repository = repository;
    }

    private final PredefinedExerciseRepository repository;

    /**
     * Obtiene la lista completa de ejercicios predefinidos en el catálogo.
     * 
     * @return Lista de todos los ejercicios.
     */
    @GetMapping
    public ResponseEntity<List<PredefinedExercise>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    /**
     * Obtiene los ejercicios predefinidos agrupados por su grupo muscular.
     * 
     * @return Mapa donde la clave es el nombre del grupo muscular y el valor es la lista de ejercicios.
     */
    @GetMapping("/grouped")
    public ResponseEntity<Map<String, List<PredefinedExercise>>> getGroupedByCategory() {
        List<PredefinedExercise> exercises = repository.findAll();
        Map<String, List<PredefinedExercise>> grouped = exercises.stream()
                .collect(Collectors.groupingBy(e -> e.getMuscleGroup().name()));
        return ResponseEntity.ok(grouped);
    }
}
