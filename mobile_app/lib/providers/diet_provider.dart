import 'package:flutter/material.dart';
import '../models/diet.dart';
import '../network/diet_service.dart';

/// Proveedor para la gestión de las dietas del cliente.
class DietProvider extends ChangeNotifier {
  /// Lista de dietas cargadas.
  List<Diet> _diets = [];
  
  /// Indica si se están cargando datos.
  bool _isLoading = false;
  
  /// Mensaje de error.
  String? _errorMessage;

  /// Obtiene las dietas.
  List<Diet> get diets => _diets;
  
  /// Obtiene el estado de carga.
  bool get isLoading => _isLoading;
  
  /// Obtiene el mensaje de error.
  String? get errorMessage => _errorMessage;

  /// Carga las dietas asociadas a un [clientId].
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

  /// Marca o desmarca una comida como completada.
  Future<void> toggleMealCompletion(int mealId) async {
    final updatedMeal = await DietService.toggleMealCompletion(mealId);
    if (updatedMeal != null) {
      // Buscar y actualizar en la lista local para evitar recargar todo
      for (var diet in _diets) {
        final index = diet.meals.indexWhere((m) => m.id == mealId);
        if (index != -1) {
          diet.meals[index] = updatedMeal;
          notifyListeners();
          break;
        }
      }
    }
  }
}
