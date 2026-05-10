import 'dart:convert';
import 'api_client.dart';
import '../utils/token_utils.dart';

/// Servicio encargado de la autenticación de usuarios.
/// 
/// Proporciona métodos para el inicio de sesión, registro y cierre de sesión,
/// gestionando el almacenamiento de tokens JWT.
class AuthService {
  /// Endpoint para el inicio de sesión.
  static const String loginEndpoint = '/auth/login';
  
  /// Endpoint para el registro de nuevos usuarios.
  static const String registerEndpoint = '/auth/register';

  /// Realiza el inicio de sesión con [email] y [password].
  /// 
  /// Devuelve un mapa con el estado de la operación y los datos del usuario si tiene éxito.
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

  /// Registra un nuevo usuario con [name], [email] y [password].
  /// 
  /// Devuelve un mapa con el estado del registro.
  static Future<Map<String, dynamic>> register({
    required String name,
    required String email,
    required String password,
  }) async {
    try {
      final response = await ApiClient.post(
        registerEndpoint,
        {
          'name': name,
          'email': email,
          'password': password,
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

  /// Cierra la sesión del usuario eliminando los tokens almacenados.
  static Future<void> logout() async {
    await TokenUtils.clearTokens();
  }

  /// Comprueba si el usuario está actualmente autenticado.
  static Future<bool> isAuthenticated() async {
    final token = await TokenUtils.getToken();
    return token != null && token.isNotEmpty;
  }
}

