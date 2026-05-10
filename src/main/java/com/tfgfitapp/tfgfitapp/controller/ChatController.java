package com.tfgfitapp.tfgfitapp.controller;

import com.tfgfitapp.tfgfitapp.dto.MessageDto;
import com.tfgfitapp.tfgfitapp.dto.SendMessageRequest;
import com.tfgfitapp.tfgfitapp.dto.UserProfileResponse;
import com.tfgfitapp.tfgfitapp.entity.User;
import com.tfgfitapp.tfgfitapp.service.MessageService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para la mensajería interna y chat.
 * 
 * Gestiona la visualización de la página de chat y los endpoints de la API
 * para la recuperación de contactos, historial de mensajes y envío de nuevos mensajes.
 */
@Controller
public class ChatController {

    public ChatController(MessageService messageService) {
        this.messageService = messageService;
    }

    private final MessageService messageService;

    /**
     * Renderiza la página principal del chat.
     * 
     * @return El nombre de la vista (Thymeleaf) para el chat.
     */
    @GetMapping("/dashboard/chat")
    public String chatPage() {
        return "dashboard/chat";
    }

    /**
     * Obtiene la lista de contactos permitidos para el usuario autenticado.
     * 
     * @param currentUser Usuario que realiza la petición.
     * @return 200 OK con la lista de perfiles de contactos disponibles.
     */
    @GetMapping("/api/chat/contacts")
    @ResponseBody
    public ResponseEntity<List<UserProfileResponse>> getContacts(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(messageService.getAllowedContacts(currentUser));
    }

    /**
     * Obtiene el historial de mensajes (conversación) con un usuario específico.
     * 
     * @param currentUser Usuario autenticado.
     * @param userId Identificador del otro participante en la conversación.
     * @return 200 OK con la lista cronológica de mensajes.
     */
    @GetMapping("/api/chat/messages/{userId}")
    @ResponseBody
    public ResponseEntity<List<MessageDto>> getMessages(@AuthenticationPrincipal User currentUser, @PathVariable Long userId) {
        return ResponseEntity.ok(messageService.getConversation(currentUser, userId));
    }

    /**
     * Envía un nuevo mensaje a otro usuario.
     * 
     * @param currentUser Usuario autenticado que envía el mensaje.
     * @param request Datos del mensaje (destinatario y contenido).
     * @return 200 OK con el mensaje enviado y su marca de tiempo.
     */
    @PostMapping("/api/chat/messages")
    @ResponseBody
    public ResponseEntity<MessageDto> sendMessage(@AuthenticationPrincipal User currentUser, @RequestBody SendMessageRequest request) {
        return ResponseEntity.ok(messageService.sendMessage(currentUser, request));
    }
}
