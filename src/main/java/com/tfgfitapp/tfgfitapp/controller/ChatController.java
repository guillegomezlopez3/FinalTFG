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

@Controller
public class ChatController {

    public ChatController(MessageService messageService) {
        this.messageService = messageService;
    }

    private final MessageService messageService;

    @GetMapping("/dashboard/chat")
    public String chatPage() {
        return "dashboard/chat";
    }

    @GetMapping("/api/chat/contacts")
    @ResponseBody
    public ResponseEntity<List<UserProfileResponse>> getContacts(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(messageService.getAllowedContacts(currentUser));
    }

    @GetMapping("/api/chat/messages/{userId}")
    @ResponseBody
    public ResponseEntity<List<MessageDto>> getMessages(@AuthenticationPrincipal User currentUser, @PathVariable Long userId) {
        return ResponseEntity.ok(messageService.getConversation(currentUser, userId));
    }

    @PostMapping("/api/chat/messages")
    @ResponseBody
    public ResponseEntity<MessageDto> sendMessage(@AuthenticationPrincipal User currentUser, @RequestBody SendMessageRequest request) {
        return ResponseEntity.ok(messageService.sendMessage(currentUser, request));
    }
}
