import 'package:shared_preferences/shared_preferences.dart';

/// Utilidades para la gestión persistente de tokens JWT.
/// 
/// Utiliza `SharedPreferences` para almacenar y recuperar los tokens de acceso y refresco.
class TokenUtils {
  static const String _tokenKey = 'jwt_token';
  static const String _refreshTokenKey = 'refresh_token';

  /// Guarda el [token] de acceso y opcionalmente el [refreshToken] en el almacenamiento local.
  static Future<void> saveTokens(String token, {String? refreshToken}) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString(_tokenKey, token);
    if (refreshToken != null) {
      await prefs.setString(_refreshTokenKey, refreshToken);
    }
  }

  /// Recupera el token de acceso actual.
  static Future<String?> getToken() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString(_tokenKey);
  }

  /// Recupera el token de refresco actual.
  static Future<String?> getRefreshToken() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString(_refreshTokenKey);
  }

  /// Elimina todos los tokens del almacenamiento (utilizado en logout).
  static Future<void> clearTokens() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove(_tokenKey);
    await prefs.remove(_refreshTokenKey);
  }
}

