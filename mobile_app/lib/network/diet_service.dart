import 'dart:convert';
import 'api_client.dart';
import '../models/diet.dart';

class DietService {
  static const String dietsEndpoint = '/diets';

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
}
