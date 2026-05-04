import 'dart:convert';
import 'api_client.dart';
import '../models/message.dart';

class ChatService {
  static const String chatEndpoint = '/chat';

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
