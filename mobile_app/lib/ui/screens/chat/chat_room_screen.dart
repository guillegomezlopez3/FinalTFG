import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../../providers/chat_provider.dart';
import '../../../providers/user_provider.dart';
import '../../../utils/app_colors.dart';
import '../../../models/message.dart';

class ChatRoomScreen extends StatefulWidget {
  final ChatContact contact;

  const ChatRoomScreen({super.key, required this.contact});

  @override
  State<ChatRoomScreen> createState() => _ChatRoomScreenState();
}

class _ChatRoomScreenState extends State<ChatRoomScreen> {
  final _messageController = TextEditingController();

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      context.read<ChatProvider>().fetchMessages(widget.contact.id);
    });
  }

  void _handleSendMessage() async {
    final content = _messageController.text.trim();
    if (content.isNotEmpty) {
      final success = await context.read<ChatProvider>().sendMessage(widget.contact.id, content);
      if (success) {
        _messageController.clear();
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    final currentUserId = context.read<UserProvider>().userProfile?.id;

    return Scaffold(
      appBar: AppBar(
        title: Row(
          children: [
            CircleAvatar(
              radius: 16,
              backgroundImage: widget.contact.avatar != null ? NetworkImage(widget.contact.avatar!) : null,
              child: widget.contact.avatar == null ? const Icon(Icons.person, size: 16) : null,
            ),
            const SizedBox(width: 12),
            Text(widget.contact.name),
          ],
        ),
      ),
      body: Column(
        children: [
          Expanded(
            child: Consumer<ChatProvider>(
              builder: (context, chat, _) {
                return ListView.builder(
                  padding: const EdgeInsets.all(16),
                  reverse: true, // For chat feel
                  itemCount: chat.messages.length,
                  itemBuilder: (context, index) {
                    // Reverse index because of reverse: true
                    final message = chat.messages[chat.messages.length - 1 - index];
                    final isMe = message.senderId == currentUserId;
                    return _ChatBubble(message: message, isMe: isMe);
                  },
                );
              },
            ),
          ),
          _buildInputSection(),
        ],
      ),
    );
  }

  Widget _buildInputSection() {
    return Container(
      padding: const EdgeInsets.all(12),
      decoration: const BoxDecoration(
        color: Colors.white,
        border: Border(top: BorderSide(color: AppColors.border)),
      ),
      child: SafeArea(
        child: Row(
          children: [
            Expanded(
              child: TextField(
                controller: _messageController,
                decoration: InputDecoration(
                  hintText: 'Escribe un mensaje...',
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(24),
                    borderSide: BorderSide.none,
                  ),
                  filled: true,
                  fillColor: AppColors.background,
                  contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                ),
              ),
            ),
            const SizedBox(width: 8),
            IconButton(
              icon: const Icon(Icons.send, color: AppColors.primary),
              onPressed: _handleSendMessage,
            ),
          ],
        ),
      ),
    );
  }
}

class _ChatBubble extends StatelessWidget {
  final ChatMessage message;
  final bool isMe;

  const _ChatBubble({required this.message, required this.isMe});

  @override
  Widget build(BuildContext context) {
    return Align(
      alignment: isMe ? Alignment.centerRight : Alignment.centerLeft,
      child: Container(
        margin: const EdgeInsets.only(bottom: 12),
        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 10),
        decoration: BoxDecoration(
          color: isMe ? AppColors.primary : Colors.white,
          borderRadius: BorderRadius.only(
            topLeft: const Radius.circular(16),
            topRight: const Radius.circular(16),
            bottomLeft: Radius.circular(isMe ? 16 : 0),
            bottomRight: Radius.circular(isMe ? 0 : 16),
          ),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withOpacity(0.05),
              blurRadius: 4,
              offset: const Offset(0, 2),
            ),
          ],
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.end,
          children: [
            Text(
              message.content,
              style: TextStyle(
                color: isMe ? Colors.white : AppColors.text,
              ),
            ),
            const SizedBox(height: 4),
            Text(
              message.timestamp.split('T')[1].substring(0, 5), // Simple time format
              style: TextStyle(
                color: (isMe ? Colors.white : AppColors.textMuted).withOpacity(0.7),
                fontSize: 10,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
