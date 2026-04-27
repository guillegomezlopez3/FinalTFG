import 'dart:convert';
import 'api_client.dart';
import '../utils/token_utils.dart';

class AuthService {
  static const String loginEndpoint = '/auth/login';
  static const String registerEndpoint = '/auth/register';

  static Future<Map<String, dynamic>> login(String email, String password) async {
    try {
      final response = await ApiClient.post(
        loginEndpoint,
        {'email': email, 'password': password},
        requiresAuth: false,
      );

      if (response.statusCode == 200) {
        final data = json.decode(utf8.decode(response.bodyBytes));

        final String accessToken = data['accessToken'] ?? data['token'];
        final String? refreshToken = data['refreshToken'];

        if (accessToken.isNotEmpty) {
          await TokenUtils.saveTokens(accessToken, refreshToken: refreshToken);
          return {'success': true, 'data': data};
        }
      }

      return {'success': false, 'message': 'Credenciales no válidas o error de servidor.'};
    } catch (e) {
      return {'success': false, 'message': 'Error de conexión: $e'};
    }
  }

  static Future<Map<String, dynamic>> register({
    required String name,
    required String email,
    required String password,
    required String role, // "CLIENT" o "TRAINER"
  }) async {
    try {
      final response = await ApiClient.post(
        registerEndpoint,
        {
          'name': name,
          'email': email,
          'password': password,
          'role': role,
        },
        requiresAuth: false,
      );

      if (response.statusCode == 200 || response.statusCode == 201) {
        final data = json.decode(utf8.decode(response.bodyBytes));
        final String accessToken = data['accessToken'] ?? data['token'];

        if (accessToken.isNotEmpty) {
          await TokenUtils.saveTokens(accessToken);
          return {'success': true, 'data': data};
        }
      }

      return {'success': false, 'message': 'Error al registrar la cuenta.'};
    } catch (e) {
      return {'success': false, 'message': 'Error de conexión: $e'};
    }
  }

  static Future<void> logout() async {
    await TokenUtils.clearTokens();
  }

  static Future<bool> isAuthenticated() async {
    final token = await TokenUtils.getToken();
    return token != null && token.isNotEmpty;
  }
}

