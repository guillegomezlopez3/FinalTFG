import 'package:flutter/foundation.dart';
import '../models/progress_record.dart';
import '../network/progress_service.dart';

/// Proveedor para la gestión de registros de progreso.
class ProgressProvider with ChangeNotifier {
  /// Registros de progreso antropométrico.
  List<ProgressRecord> _records = [];
  
  /// Indica si hay una carga en curso.
  bool _isLoading = false;
  
  /// Mensaje de error.
  String? _errorMessage;

  /// Obtiene los registros.
  List<ProgressRecord> get records => _records;
  
  /// Obtiene el estado de carga.
  bool get isLoading => _isLoading;
  
  /// Obtiene el mensaje de error.
  String? get errorMessage => _errorMessage;

  /// Carga el historial de progreso del cliente
  Future<void> fetchMyRecords() async {
    _isLoading = true;
    _errorMessage = null;
    notifyListeners();

    try {
      _records = await ProgressService.getMyRecords();
    } catch (e) {
      _errorMessage = 'Error al cargar el progreso: $e';
      if (kDebugMode) {
        print(_errorMessage);
      }
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  /// Añade un nuevo registro de progreso
  Future<bool> addRecord(ProgressRecord record) async {
    _isLoading = true;
    _errorMessage = null;
    notifyListeners();

    try {
      final newRecord = await ProgressService.createRecord(record);
      // Lo añadimos al inicio de la lista (el backend suele enviar el más reciente primero)
      _records.insert(0, newRecord);
      return true;
    } catch (e) {
      _errorMessage = 'Error al guardar el registro: $e';
      if (kDebugMode) {
        print(_errorMessage);
      }
      return false;
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }
}
