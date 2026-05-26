import 'package:flutter/material.dart';
import '../network/auth_service.dart';

enum AuthStatus { authenticated, unauthenticated, authenticating, requiresPayment }

/// Proveedor para la gestión del estado de autenticación.
class AuthProvider extends ChangeNotifier {
  /// Estado actual de la autenticación.
  AuthStatus _status = AuthStatus.authenticating;

  /// Mensaje de error en caso de fallo.
  String? _errorMessage;

  /// URL de checkout de Stripe si el cliente necesita pagar la suscripción.
  String? _checkoutUrl;

  /// Obtiene el estado actual.
  AuthStatus get status => _status;

  /// Obtiene el mensaje de error.
  String? get errorMessage => _errorMessage;

  /// Obtiene la URL de pago de Stripe (solo cuando requiresPayment).
  String? get checkoutUrl => _checkoutUrl;

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
  /// Si el usuario es un CLIENT sin suscripción activa, el estado cambia
  /// a [AuthStatus.requiresPayment] y [checkoutUrl] contiene el enlace de pago.
  Future<bool> login(String email, String password) async {
    _status = AuthStatus.authenticating;
    _errorMessage = null;
    _checkoutUrl = null;
    notifyListeners();

    final result = await AuthService.login(email, password);

    if (result['success'] == true) {
      // ¿El servidor indica que el cliente debe pagar?
      if (result['requiresPayment'] == true) {
        _checkoutUrl = result['checkoutUrl'] as String?;
        _status = AuthStatus.requiresPayment;
        notifyListeners();
        return true; // login técnicamente exitoso, pero con paywall
      }
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

    if (result['success'] == true) {
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

  /// Vuelve al estado de no autenticado (para que el cliente pueda reintentar
  /// el login tras completar el pago de Stripe).
  void resetToUnauthenticated() {
    _status = AuthStatus.unauthenticated;
    _checkoutUrl = null;
    _errorMessage = null;
    notifyListeners();
  }

  /// Cierra la sesión y limpia el estado local.
  Future<void> logout() async {
    await AuthService.logout();
    _status = AuthStatus.unauthenticated;
    _checkoutUrl = null;
    notifyListeners();
  }
}
