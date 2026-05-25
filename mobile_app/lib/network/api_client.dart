import 'dart:convert';
import 'package:http/http.dart' as http;
import '../utils/token_utils.dart';

/// Cliente HTTP base para realizar peticiones a la API del backend.
/// 
/// Gestiona la URL base, los headers de autenticación JWT y los tiempos de espera.
class ApiClient {
  /// URL base para la comunicación con el servidor.
  /// 
  /// Para desarrollo local (Emulador Android): 'http://10.0.2.2:8081/api'
  /// Para desarrollo local (iOS Emulador / Web): 'http://localhost:8081/api'
  /// Para celular físico en red Wi-Fi: 'http://<TU_IP_LOCAL>:8081/api'
  /// PARA PRODUCCIÓN EN LA NUBE: Cambiar a la URL de tu servidor (ej. 'https://tu-dominio.com/api')
  static const String baseUrl = 'http://10.0.2.2:8081/api';

  /// Genera los headers necesarios para la petición.
  /// 
  /// Si [requiresAuth] es true, intenta adjuntar el token JWT almacenado.
  static Future<Map<String, String>> _getHeaders({bool requiresAuth = true}) async {
    final headers = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    };

    if (requiresAuth) {
      final token = await TokenUtils.getToken();
      if (token != null) {
        headers['Authorization'] = 'Bearer $token';
      }
    }
    return headers;
  }

  /// Realiza una petición GET al [endpoint] especificado.
  static Future<http.Response> get(String endpoint, {bool requiresAuth = true}) async {
    final url = Uri.parse('$baseUrl$endpoint');
    final headers = await _getHeaders(requiresAuth: requiresAuth);
    final response = await http.get(url, headers: headers).timeout(const Duration(seconds: 10));
    return response;
  }

  /// Realiza una petición POST al [endpoint] con un [body] JSON.
  static Future<http.Response> post(String endpoint, dynamic body, {bool requiresAuth = true}) async {
    final url = Uri.parse('$baseUrl$endpoint');
    final headers = await _getHeaders(requiresAuth: requiresAuth);
    final response = await http.post(
      url,
      headers: headers,
      body: json.encode(body),
    ).timeout(const Duration(seconds: 10));
    return response;
  }

  /// Realiza una petición PUT al [endpoint] para actualizar un recurso completo.
  static Future<http.Response> put(String endpoint, dynamic body, {bool requiresAuth = true}) async {
    final url = Uri.parse('$baseUrl$endpoint');
    final headers = await _getHeaders(requiresAuth: requiresAuth);
    final response = await http.put(
      url,
      headers: headers,
      body: json.encode(body),
    ).timeout(const Duration(seconds: 10));
    return response;
  }

  /// Realiza una petición PATCH al [endpoint] para actualizaciones parciales.
  static Future<http.Response> patch(String endpoint, dynamic body, {bool requiresAuth = true}) async {
    final url = Uri.parse('$baseUrl$endpoint');
    final headers = await _getHeaders(requiresAuth: requiresAuth);
    final response = await http.patch(
      url,
      headers: headers,
      body: json.encode(body),
    ).timeout(const Duration(seconds: 10));
    return response;
  }

  /// Realiza una petición DELETE al [endpoint].
  static Future<http.Response> delete(String endpoint, {bool requiresAuth = true}) async {
    final url = Uri.parse('$baseUrl$endpoint');
    final headers = await _getHeaders(requiresAuth: requiresAuth);
    final response = await http.delete(url, headers: headers).timeout(const Duration(seconds: 10));
    return response;
  }
}
