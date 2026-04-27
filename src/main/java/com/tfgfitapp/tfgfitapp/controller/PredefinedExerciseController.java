package com.tfgfitapp.tfgfitapp.controller;

import com.tfgfitapp.tfgfitapp.entity.PredefinedExercise;
import com.tfgfitapp.tfgfitapp.repository.PredefinedExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/predefined-exercises")
@RequiredArgsConstructor
public class PredefinedExerciseController {

    private final PredefinedExerciseRepository repository;

    @GetMapping
    public ResponseEntity<List<PredefinedExercise>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/grouped")
    public ResponseEntity<Map<String, List<PredefinedExercise>>> getGroupedByCategory() {
        List<PredefinedExercise> exercises = repository.findAll();
        Map<String, List<PredefinedExercise>> grouped = exercises.stream()
                .collect(Collectors.groupingBy(e -> e.getMuscleGroup().name()));
        return ResponseEntity.ok(grouped);
    }
}
