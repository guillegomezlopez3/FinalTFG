import 'package:flutter/material.dart';
import '../models/message.dart';
import '../network/chat_service.dart';

class ChatProvider extends ChangeNotifier {
  List<ChatContact> _contacts = [];
  List<ChatMessage> _messages = [];
  bool _isLoading = false;

  List<ChatContact> get contacts => _contacts;
  List<ChatMessage> get messages => _messages;
  bool get isLoading => _isLoading;

  Future<void> fetchContacts() async {
    _isLoading = true;
    notifyListeners();
    _contacts = await ChatService.getContacts();
    _isLoading = false;
    notifyListeners();
  }

  Future<void> fetchMessages(int otherUserId) async {
    _messages = await ChatService.getMessages(otherUserId);
    notifyListeners();
  }

  Future<bool> sendMessage(int receiverId, String content) async {
    final success = await ChatService.sendMessage(receiverId, content);
    if (success) {
      await fetchMessages(receiverId);
    }
    return success;
  }
}
