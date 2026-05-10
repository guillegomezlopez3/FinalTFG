import 'package:flutter/material.dart';
import '../models/user_profile.dart';
import '../network/user_service.dart';

/// Proveedor para la gestión del perfil de usuario.
class UserProvider extends ChangeNotifier {
  /// Datos del perfil del usuario.
  UserProfileResponse? _userProfile;
  
  /// Indica si se está cargando el perfil.
  bool _isLoading = false;
  
  /// Mensaje de error.
  String? _errorMessage;

  /// Obtiene el perfil.
  UserProfileResponse? get userProfile => _userProfile;
  
  /// Obtiene el estado de carga.
  bool get isLoading => _isLoading;
  
  /// Obtiene el mensaje de error.
  String? get errorMessage => _errorMessage;

  /// Carga el perfil del usuario desde el servidor.
  Future<void> fetchProfile() async {
    _isLoading = true;
    _errorMessage = null;
    notifyListeners();

    try {
      final profile = await UserService.getMyProfile();
      if (profile != null) {
        _userProfile = profile;
      } else {
        _errorMessage = 'No se pudo cargar el perfil.';
      }
    } catch (e) {
      _errorMessage = 'Error de conexión al cargar el perfil.';
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  /// Limpia los datos del perfil (e.g., al cerrar sesión).
  void clearProfile() {
    _userProfile = null;
    notifyListeners();
  }
}
