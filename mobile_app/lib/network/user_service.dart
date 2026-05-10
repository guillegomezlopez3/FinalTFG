import 'dart:convert';
import 'api_client.dart';
import '../models/user_profile.dart';

/// Servicio para la gestión de información del perfil de usuario.
class UserService {
  /// Endpoint para obtener el perfil del usuario autenticado.
  static const String meEndpoint = '/me';

  /// Obtiene el perfil completo del usuario actual.
  static Future<UserProfileResponse?> getMyProfile() async {
    try {
      final response = await ApiClient.get(meEndpoint);

      if (response.statusCode == 200) {
        final data = json.decode(utf8.decode(response.bodyBytes));
        return UserProfileResponse.fromJson(data);
      }
      return null;
    } catch (e) {
      return null;
    }
  }
}

