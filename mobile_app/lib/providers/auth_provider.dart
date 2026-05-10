import 'package:flutter/material.dart';
import '../network/auth_service.dart';

enum AuthStatus { authenticated, unauthenticated, authenticating }

/// Proveedor para la gestión del estado de autenticación.
class AuthProvider extends ChangeNotifier {
  /// Estado actual de la autenticación.
  AuthStatus _status = AuthStatus.authenticating;
  
  /// Mensaje de error en caso de fallo.
  String? _errorMessage;

  /// Obtiene el estado actual.
  AuthStatus get status => _status;
  
  /// Obtiene el mensaje de error.
  String? get errorMessage => _errorMessage;

  /// Inicializa el proveedor verificando si ya existe una sesión.
  AuthProvider() {
    _checkAuth();
  }

  /// Verifica si el usuario tiene un token válido almacenado.
  Future<void> _checkAuth() async {
    final bool isAuth = await AuthService.isAuthenticated();
    _status = isAuth ? AuthStatus.authenticated : AuthStatus.unauthenticated;
    notifyListeners();
  }

  /// Inicia sesión con [email] y [password].
  Future<bool> login(String email, String password) async {
    _status = AuthStatus.authenticating;
    _errorMessage = null;
    notifyListeners();

    final result = await AuthService.login(email, password);

    if (result['success']) {
      _status = AuthStatus.authenticated;
      notifyListeners();
      return true;
    } else {
      _status = AuthStatus.unauthenticated;
      _errorMessage = result['message'];
      notifyListeners();
      return false;
    }
  }

  /// Registra un nuevo usuario con [name], [email] y [password].
  Future<Map<String, dynamic>> register({
    required String name,
    required String email,
    required String password,
  }) async {
    _status = AuthStatus.authenticating;
    _errorMessage = null;
    notifyListeners();

    final result = await AuthService.register(
      name: name,
      email: email,
      password: password,
    );

    if (result['success']) {
      _status = AuthStatus.authenticated;
      notifyListeners();
      return result;
    } else {
      _status = AuthStatus.unauthenticated;
      _errorMessage = result['message'];
      notifyListeners();
      return result;
    }
  }

  /// Cierra la sesión y limpia el estado local.
  Future<void> logout() async {
    await AuthService.logout();
    _status = AuthStatus.unauthenticated;
    notifyListeners();
  }
}
