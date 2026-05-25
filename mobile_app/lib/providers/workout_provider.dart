import 'package:flutter/material.dart';
import '../models/workout.dart';
import '../models/exercise_progress.dart';
import '../network/workout_service.dart';
import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';

/// Proveedor para la gestión de rutinas y ejercicios.
class WorkoutProvider extends ChangeNotifier {
  /// Lista de planes de entrenamiento.
  List<WorkoutPlan> _workoutPlans = [];
  
  /// Indica si se están cargando los planes.
  bool _isLoading = false;
  
  /// Mensaje de error.
  String? _errorMessage;

  /// Mapa que asocia el nombre del ejercicio con la fecha (milisegundos desde epoch) en la que se completó.
  Map<String, int> _completedExercisesMap = {};

  WorkoutProvider() {
    _loadCompletedExercises();
  }

  /// Obtiene los planes.
  List<WorkoutPlan> get workoutPlans => _workoutPlans;
  
  /// Obtiene el estado de carga.
  bool get isLoading => _isLoading;
  
  /// Obtiene el mensaje de error.
  String? get errorMessage => _errorMessage;
  
  /// Obtiene los nombres de los ejercicios que están completados y no han caducado (menos de 24h).
  Set<String> get completedExerciseNames => _completedExercisesMap.keys.toSet();

  /// Carga desde SharedPreferences el estado de los ejercicios completados y elimina los que tienen > 24 horas
  Future<void> _loadCompletedExercises() async {
    final prefs = await SharedPreferences.getInstance();
    final String? savedData = prefs.getString('completed_exercises');

    if (savedData != null) {
      try {
        final decodedMap = json.decode(savedData) as Map<String, dynamic>;
        final now = DateTime.now().millisecondsSinceEpoch;
        final int twentyFourHours = 24 * 60 * 60 * 1000;

        final Map<String, int> activeExercises = {};

        decodedMap.forEach((key, value) {
          final int timestamp = value as int;
          // Si el tiempo transcurrido es menor a 24h, lo mantenemos
          if (now - timestamp < twentyFourHours) {
            activeExercises[key] = timestamp;
          }
        });

        _completedExercisesMap = activeExercises;

        // Guardamos de nuevo para "limpiar" los caducados del disco
        prefs.setString('completed_exercises', json.encode(_completedExercisesMap));
        notifyListeners();
      } catch (e) {
        // En caso de error, reiniciamos el mapa
        _completedExercisesMap = {};
      }
    }
  }

  /// Guarda en disco el mapa de completados actual
  Future<void> _saveCompletedExercisesToStorage() async {
    final prefs = await SharedPreferences.getInstance();
    prefs.setString('completed_exercises', json.encode(_completedExercisesMap));
  }

  /// Carga los planes de un [clientId].
  Future<void> fetchClientWorkoutPlans(int clientId) async {
    _isLoading = true;
    _errorMessage = null;
    notifyListeners();

    try {
      _workoutPlans = await WorkoutService.getClientWorkoutPlans(clientId);
      if (_workoutPlans.isEmpty) {
        _errorMessage = 'No hay planes de entrenamiento asignados.';
      }
    } catch (e) {
      _errorMessage = 'Error al cargar los entrenamientos.';
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  /// Guarda el progreso de un ejercicio y lo marca como completado.
  Future<bool> logProgress(ExerciseProgress progress) async {
    final success = await WorkoutService.logExerciseProgress(progress);
    if (success) {
      _completedExercisesMap[progress.exerciseName] = DateTime.now().millisecondsSinceEpoch;
      await _saveCompletedExercisesToStorage();
      notifyListeners();
    }
    return success;
  }

  /// Obtiene el historial de un ejercicio.
  Future<List<ExerciseProgress>> getExerciseHistory(String name, {int? predefinedId}) async {
    return await WorkoutService.getExerciseProgressHistory(name, predefinedId: predefinedId);
  }
}
