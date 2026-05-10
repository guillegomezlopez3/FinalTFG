import 'dart:convert';
import 'api_client.dart';
import '../models/diet.dart';

/// Servicio para la gestión de planes nutricionales (dietas).
class DietService {
  /// Endpoint base para las dietas.
  static const String dietsEndpoint = '/diets';

  /// Obtiene todas las dietas asignadas a un cliente identificado por [clientId].
  static Future<List<Diet>> getClientDiets(int clientId) async {
    try {
      final response = await ApiClient.get('$dietsEndpoint/client/$clientId');

      if (response.statusCode == 200) {
        final List<dynamic> data = json.decode(utf8.decode(response.bodyBytes));
        return data.map((json) => Diet.fromJson(json)).toList();
      }
      return [];
    } catch (e) {
      return [];
    }
  }

  /// Obtiene los detalles completos de una dieta específica por su [dietId].
  static Future<Diet?> getDietDetails(int dietId) async {
    try {
      final response = await ApiClient.get('$dietsEndpoint/$dietId');

      if (response.statusCode == 200) {
        final data = json.decode(utf8.decode(response.bodyBytes));
        return Diet.fromJson(data);
      }
      return null;
    } catch (e) {
      return null;
    }
  }

  /// Cambia el estado de completado de una comida específica identificada por [mealId].
  static Future<DietMeal?> toggleMealCompletion(int mealId) async {
    try {
      final response = await ApiClient.patch('$dietsEndpoint/meals/$mealId/toggle', {});
      if (response.statusCode == 200) {
        final data = json.decode(utf8.decode(response.bodyBytes));
        return DietMeal.fromJson(data);
      }
      return null;
    } catch (e) {
      return null;
    }
  }
}
