import 'package:flutter/material.dart';
import '../models/workout.dart';
import '../network/workout_service.dart';

class WorkoutProvider extends ChangeNotifier {
  List<WorkoutPlan> _workoutPlans = [];
  bool _isLoading = false;
  String? _errorMessage;

  List<WorkoutPlan> get workoutPlans => _workoutPlans;
  bool get isLoading => _isLoading;
  String? get errorMessage => _errorMessage;

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
}
