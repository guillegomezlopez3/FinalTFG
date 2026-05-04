import 'package:flutter/material.dart';
import '../models/user_profile.dart';
import '../network/user_service.dart';

class UserProvider extends ChangeNotifier {
  UserProfileResponse? _userProfile;
  bool _isLoading = false;
  String? _errorMessage;

  UserProfileResponse? get userProfile => _userProfile;
  bool get isLoading => _isLoading;
  String? get errorMessage => _errorMessage;

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

  void clearProfile() {
    _userProfile = null;
    notifyListeners();
  }
}
