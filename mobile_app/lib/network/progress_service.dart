import 'dart:convert';
import '../models/progress_record.dart';
import 'api_client.dart';

/// Servicio para el seguimiento del progreso antropométrico del cliente.
class ProgressService {
  /// Endpoint base para el progreso.
  static const String progressEndpoint = '/progress';

  /// Obtiene los registros de progreso del cliente actual.
  static Future<List<ProgressRecord>> getMyRecords() async {
    final response = await ApiClient.get('$progressEndpoint/me');
    
    if (response.statusCode == 200) {
      final List<dynamic> data = json.decode(utf8.decode(response.bodyBytes));
      return data.map((e) => ProgressRecord.fromJson(e)).toList();
    }
    
    throw Exception('Error al obtener registros de progreso');
  }

  /// Crea un nuevo registro de progreso para el cliente actual.
  static Future<ProgressRecord> createRecord(ProgressRecord record) async {
    final response = await ApiClient.post(
      progressEndpoint,
      record.toJson(),
    );
    
    if (response.statusCode == 201 || response.statusCode == 200) {
      final data = json.decode(utf8.decode(response.bodyBytes));
      return ProgressRecord.fromJson(data);
    }

    throw Exception('Error al crear registro de progreso');
  }
}
