import 'dart:convert';
import 'api_client.dart';
import '../models/workout.dart';

class WorkoutService {
  static const String workoutPlansEndpoint = '/workout-plans';

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
}
