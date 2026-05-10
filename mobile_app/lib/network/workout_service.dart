import 'dart:convert';
import 'api_client.dart';
import '../models/workout.dart';
import '../models/exercise_progress.dart';

/// Servicio para la gestión de planes de entrenamiento y registros de ejercicios.
class WorkoutService {
  /// Endpoint base para los planes de entrenamiento.
  static const String workoutPlansEndpoint = '/workout-plans';

  /// Obtiene los planes de entrenamiento asignados a un cliente identificado por [clientId].
  static Future<List<WorkoutPlan>> getClientWorkoutPlans(int clientId) async {
    try {
      final response = await ApiClient.get('$workoutPlansEndpoint/client/$clientId');

      if (response.statusCode == 200) {
        final List<dynamic> data = json.decode(utf8.decode(response.bodyBytes));
        return data.map((json) => WorkoutPlan.fromJson(json)).toList();
      }
      return [];
    } catch (e) {
      return [];
    }
  }

  /// Registra el progreso (peso/reps) realizado en un ejercicio específico.
  static Future<bool> logExerciseProgress(ExerciseProgress progress) async {
    try {
      final response = await ApiClient.post('/exercise-progress', progress.toJson());
      // El backend devuelve 201 Created o 200 OK
      return response.statusCode == 201 || response.statusCode == 200;
    } catch (e) {
      return false;
    }
  }

  /// Obtiene el historial de marcas para un ejercicio por nombre e ID predefinido opcional.
  static Future<List<ExerciseProgress>> getExerciseProgressHistory(String name, {int? predefinedId}) async {
    try {
      String url = '/exercise-progress/exercise?name=${Uri.encodeComponent(name)}';
      if (predefinedId != null) {
        url += '&predefinedExerciseId=$predefinedId';
      }
      
      final response = await ApiClient.get(url);
      if (response.statusCode == 200) {
        final List<dynamic> data = json.decode(utf8.decode(response.bodyBytes));
        return data.map((json) => ExerciseProgress.fromJson(json)).toList();
      }
      return [];
    } catch (e) {
      return [];
    }
  }
}
