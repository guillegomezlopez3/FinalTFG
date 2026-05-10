import 'package:flutter/material.dart';
import '../models/message.dart';
import '../network/chat_service.dart';

/// Proveedor para la gestión de la mensajería en tiempo real (UI).
class ChatProvider extends ChangeNotifier {
  /// Lista de contactos disponibles.
  List<ChatContact> _contacts = [];
  
  /// Historial de mensajes de la conversación activa.
  List<ChatMessage> _messages = [];
  
  /// Indica si hay una operación de carga en curso.
  bool _isLoading = false;

  /// Obtiene los contactos.
  List<ChatContact> get contacts => _contacts;
  
  /// Obtiene los mensajes.
  List<ChatMessage> get messages => _messages;
  
  /// Obtiene el estado de carga.
  bool get isLoading => _isLoading;

  /// Carga la lista de contactos desde el servidor.
  Future<void> fetchContacts() async {
    _isLoading = true;
    notifyListeners();
    _contacts = await ChatService.getContacts();
    _isLoading = false;
    notifyListeners();
  }

  /// Carga los mensajes de un usuario específico [otherUserId].
  Future<void> fetchMessages(int otherUserId) async {
    _messages = await ChatService.getMessages(otherUserId);
    notifyListeners();
  }

  /// Envía un mensaje y actualiza la lista local.
  Future<bool> sendMessage(int receiverId, String content) async {
    final success = await ChatService.sendMessage(receiverId, content);
    if (success) {
      await fetchMessages(receiverId);
    }
    return success;
  }
}
