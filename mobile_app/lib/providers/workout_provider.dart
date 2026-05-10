import 'package:flutter/material.dart';
import '../models/workout.dart';
import '../models/exercise_progress.dart';
import '../network/workout_service.dart';

/// Proveedor para la gestión de rutinas y ejercicios.
class WorkoutProvider extends ChangeNotifier {
  /// Lista de planes de entrenamiento.
  List<WorkoutPlan> _workoutPlans = [];
  
  /// Indica si se están cargando los planes.
  bool _isLoading = false;
  
  /// Mensaje de error.
  String? _errorMessage;

  /// Conjunto de nombres de ejercicios completados en la sesión actual.
  final Set<String> _completedExerciseNames = {};

  /// Obtiene los planes.
  List<WorkoutPlan> get workoutPlans => _workoutPlans;
  
  /// Obtiene el estado de carga.
  bool get isLoading => _isLoading;
  
  /// Obtiene el mensaje de error.
  String? get errorMessage => _errorMessage;
  
  /// Obtiene los ejercicios completados.
  Set<String> get completedExerciseNames => _completedExerciseNames;

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
      _completedExerciseNames.add(progress.exerciseName);
      notifyListeners();
    }
    return success;
  }

  /// Obtiene el historial de un ejercicio.
  Future<List<ExerciseProgress>> getExerciseHistory(String name, {int? predefinedId}) async {
    return await WorkoutService.getExerciseProgressHistory(name, predefinedId: predefinedId);
  }
}
