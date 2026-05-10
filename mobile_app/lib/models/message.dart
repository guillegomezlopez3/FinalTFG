/// Modelo que representa un mensaje individual en una conversación de chat.
class ChatMessage {
  final int id;
  final int senderId;
  final int receiverId;
  final String content;
  final String timestamp;
  final bool isRead;

  ChatMessage({
    required this.id,
    required this.senderId,
    required this.receiverId,
    required this.content,
    required this.timestamp,
    required this.isRead,
  });

  factory ChatMessage.fromJson(Map<String, dynamic> json) {
    return ChatMessage(
      id: json['id'],
      senderId: json['senderId'] ?? 0,
      receiverId: json['receiverId'] ?? 0,
      content: json['content'] ?? '',
      timestamp: json['timestamp'] ?? '',
      isRead: json['read'] ?? json['isRead'] ?? false,
    );
  }
}

/// Modelo que representa un contacto con el que el usuario puede chatear.
class ChatContact {
  final int id;
  final String name;
  final String? avatar;
  final String? lastMessage;
  final int unreadCount;

  ChatContact({
    required this.id,
    required this.name,
    this.avatar,
    this.lastMessage,
    this.unreadCount = 0,
  });

  factory ChatContact.fromJson(Map<String, dynamic> json) {
    return ChatContact(
      id: json['id'],
      name: json['name'],
      avatar: json['avatar'],
      lastMessage: json['lastMessage'],
      unreadCount: json['unreadCount'] ?? 0,
    );
  }
}
