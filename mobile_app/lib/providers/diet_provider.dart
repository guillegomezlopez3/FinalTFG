import 'package:flutter/material.dart';
import '../models/diet.dart';
import '../network/diet_service.dart';

class DietProvider extends ChangeNotifier {
  List<Diet> _diets = [];
  bool _isLoading = false;
  String? _errorMessage;

  List<Diet> get diets => _diets;
  bool get isLoading => _isLoading;
  String? get errorMessage => _errorMessage;

  Future<void> fetchClientDiets(int clientId) async {
    _isLoading = true;
    _errorMessage = null;
    notifyListeners();

    try {
      _diets = await DietService.getClientDiets(clientId);
      if (_diets.isEmpty) {
        _errorMessage = 'No hay dietas asignadas.';
      }
    } catch (e) {
      _errorMessage = 'Error al cargar las dietas.';
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }
}
