import 'dart:convert';
import 'package:http/http.dart' as http;
import '../utils/token_utils.dart';

class ApiClient {
  // Ajusta esta URL por la IP de tu PC en la red local o localhost (10.0.2.2 en emulador Android)
  static const String baseUrl = 'http://10.0.2.2:8080/api';

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

  static Future<http.Response> get(String endpoint, {bool requiresAuth = true}) async {
    final url = Uri.parse('$baseUrl$endpoint');
    final headers = await _getHeaders(requiresAuth: requiresAuth);
    final response = await http.get(url, headers: headers);
    return response;
  }

  static Future<http.Response> post(String endpoint, dynamic body, {bool requiresAuth = true}) async {
    final url = Uri.parse('$baseUrl$endpoint');
    final headers = await _getHeaders(requiresAuth: requiresAuth);
    final response = await http.post(
      url,
      headers: headers,
      body: json.encode(body),
    );
    return response;
  }

  static Future<http.Response> put(String endpoint, dynamic body, {bool requiresAuth = true}) async {
    final url = Uri.parse('$baseUrl$endpoint');
    final headers = await _getHeaders(requiresAuth: requiresAuth);
    final response = await http.put(
      url,
      headers: headers,
      body: json.encode(body),
    );
    return response;
  }

  static Future<http.Response> delete(String endpoint, {bool requiresAuth = true}) async {
    final url = Uri.parse('$baseUrl$endpoint');
    final headers = await _getHeaders(requiresAuth: requiresAuth);
    final response = await http.delete(url, headers: headers);
    return response;
  }
}

