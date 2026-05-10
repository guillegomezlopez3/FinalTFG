import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../../providers/chat_provider.dart';
import '../../../utils/app_colors.dart';
import '../../../models/message.dart';
import 'chat_room_screen.dart';

/// Pantalla que muestra la lista de conversaciones (contactos) del usuario.
class ChatListScreen extends StatefulWidget {
  const ChatListScreen({super.key});

  @override
  State<ChatListScreen> createState() => _ChatListScreenState();
}

/// Estado de la pantalla de lista de chats que gestiona la carga de contactos.
class _ChatListScreenState extends State<ChatListScreen> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      context.read<ChatProvider>().fetchContacts();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.background,
      body: CustomScrollView(
        slivers: [
          SliverAppBar(
            expandedHeight: 120.0,
            floating: false,
            pinned: true,
            flexibleSpace: FlexibleSpaceBar(
              title: const Text(
                'Mensajes',
                style: TextStyle(
                  color: AppColors.text,
                  fontWeight: FontWeight.w900,
                ),
              ),
              centerTitle: true,
              background: Container(color: AppColors.background),
            ),
            backgroundColor: AppColors.background.withOpacity(0.8),
            elevation: 0,
            actions: [
              IconButton(
                icon: const Icon(Icons.search_rounded, color: AppColors.text),
                onPressed: () {},
              ),
            ],
          ),
          Consumer<ChatProvider>(
            builder: (context, chatProvider, child) {
              if (chatProvider.isLoading) {
                return const SliverFillRemaining(
                  child: Center(child: CircularProgressIndicator()),
                );
              }

              if (chatProvider.contacts.isEmpty) {
                return SliverFillRemaining(
                  child: Center(
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Icon(Icons.chat_bubble_outline_rounded, size: 64, color: AppColors.textMuted.withOpacity(0.2)),
                        const SizedBox(height: 16),
                        const Text('No tienes conversaciones activas.', style: TextStyle(color: AppColors.textMuted)),
                      ],
                    ),
                  ),
                );
              }

              return SliverPadding(
                padding: const EdgeInsets.symmetric(horizontal: 16),
                sliver: SliverList(
                  delegate: SliverChildBuilderDelegate(
                    (context, index) {
                      final contact = chatProvider.contacts[index];
                      return _ContactTile(contact: contact);
                    },
                    childCount: chatProvider.contacts.length,
                  ),
                ),
              );
            },
          ),
        ],
      ),
    );
  }
}

/// Elemento de lista interactivo para un contacto individual.
class _ContactTile extends StatelessWidget {
  final ChatContact contact;

  const _ContactTile({required this.contact});

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: const EdgeInsets.only(bottom: 12),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(24),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.02),
            blurRadius: 10,
            offset: const Offset(0, 4),
          ),
        ],
      ),
      child: ListTile(
        contentPadding: const EdgeInsets.all(12),
        leading: Stack(
          children: [
            CircleAvatar(
              radius: 28,
              backgroundColor: AppColors.primary.withOpacity(0.1),
              backgroundImage: contact.avatar != null ? NetworkImage(contact.avatar!) : null,
              child: contact.avatar == null ? const Icon(Icons.person, color: AppColors.primary, size: 28) : null,
            ),
            if (contact.unreadCount > 0)
              Positioned(
                right: 0,
                top: 0,
                child: Container(
                  width: 12,
                  height: 12,
                  decoration: BoxDecoration(
                    color: AppColors.primary,
                    shape: BoxShape.circle,
                    border: Border.all(color: Colors.white, width: 2),
                  ),
                ),
              ),
          ],
        ),
        title: Text(
          contact.name,
          style: const TextStyle(fontWeight: FontWeight.w800, fontSize: 16),
        ),
        subtitle: Padding(
          padding: const EdgeInsets.only(top: 4.0),
          child: Text(
            contact.lastMessage ?? 'Inicia una conversación',
            maxLines: 1,
            overflow: TextOverflow.ellipsis,
            style: TextStyle(
              color: contact.unreadCount > 0 ? AppColors.text : AppColors.textMuted,
              fontWeight: contact.unreadCount > 0 ? FontWeight.w700 : FontWeight.w500,
              fontSize: 13,
            ),
          ),
        ),
        trailing: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.end,
          children: [
            Text(
              '12:45', // Mock time, could be from contact.lastMessageTime
              style: TextStyle(color: AppColors.textMuted.withOpacity(0.5), fontSize: 11),
            ),
            if (contact.unreadCount > 0) ...[
              const SizedBox(height: 4),
              Container(
                padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                decoration: BoxDecoration(
                  color: AppColors.primary,
                  borderRadius: BorderRadius.circular(10),
                ),
                child: Text(
                  '${contact.unreadCount}',
                  style: const TextStyle(color: Colors.white, fontSize: 10, fontWeight: FontWeight.w900),
                ),
              ),
            ],
          ],
        ),
        onTap: () {
          Navigator.push(
            context,
            MaterialPageRoute(
              builder: (_) => ChatRoomScreen(contact: contact),
            ),
          );
        },
      ),
    );
  }
}

