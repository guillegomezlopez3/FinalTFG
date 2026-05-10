import 'dart:convert';
import 'api_client.dart';
import '../models/message.dart';

/// Servicio para la gestión del chat y mensajes entre usuarios.
class ChatService {
  /// Endpoint base para el chat.
  static const String chatEndpoint = '/chat';

  /// Obtiene el historial de mensajes con otro usuario identificado por [otherUserId].
  static Future<List<ChatMessage>> getMessages(int otherUserId) async {
    try {
      final response = await ApiClient.get('$chatEndpoint/messages/$otherUserId');

      if (response.statusCode == 200) {
        final List<dynamic> data = json.decode(utf8.decode(response.bodyBytes));
        return data.map((json) => ChatMessage.fromJson(json)).toList();
      }
      return [];
    } catch (e) {
      return [];
    }
  }

  /// Envía un mensaje a un destinatario identificado por [receiverId] con el [content] especificado.
  static Future<bool> sendMessage(int receiverId, String content) async {
    try {
      final response = await ApiClient.post(
        '$chatEndpoint/send',
        {'receiverId': receiverId, 'content': content},
      );
      return response.statusCode == 200 || response.statusCode == 201;
    } catch (e) {
      return false;
    }
  }

  /// Obtiene la lista de contactos (usuarios con los que se ha interactuado) para el chat.
  static Future<List<ChatContact>> getContacts() async {
    try {
      final response = await ApiClient.get('$chatEndpoint/contacts');

      if (response.statusCode == 200) {
        final List<dynamic> data = json.decode(utf8.decode(response.bodyBytes));
        return data.map((json) => ChatContact.fromJson(json)).toList();
      }
      return [];
    } catch (e) {
      return [];
    }
  }
}
