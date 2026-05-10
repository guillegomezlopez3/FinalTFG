package com.tfgfitapp.tfgfitapp.service;

import com.tfgfitapp.tfgfitapp.dto.MessageDto;
import com.tfgfitapp.tfgfitapp.dto.SendMessageRequest;
import com.tfgfitapp.tfgfitapp.dto.UserProfileResponse;
import com.tfgfitapp.tfgfitapp.entity.Message;
import com.tfgfitapp.tfgfitapp.entity.User;
import com.tfgfitapp.tfgfitapp.enumeration.Role;
import com.tfgfitapp.tfgfitapp.repository.MessageRepository;
import com.tfgfitapp.tfgfitapp.repository.UserRepository;
import com.tfgfitapp.tfgfitapp.repository.ClientRepository;
import com.tfgfitapp.tfgfitapp.repository.TrainerRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de la mensajería interna (Chat).
 * 
 * Controla la lista de contactos permitidos según el rol del usuario,
 * gestiona el envío de mensajes y recupera el historial de conversaciones.
 */
@Service
public class MessageService {

    public MessageService(MessageRepository messageRepository, UserRepository userRepository,
            ClientRepository clientRepository, TrainerRepository trainerRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.trainerRepository = trainerRepository;
    }

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final TrainerRepository trainerRepository;

    /**
     * Obtiene la lista de contactos con los que el usuario tiene permitido comunicarse.
     * 
     * @param currentUser Usuario autenticado.
     * @return Lista de perfiles de contactos (Entrenadores/Clientes/Admins).
     */
    @Transactional(readOnly = true)
    public List<UserProfileResponse> getAllowedContacts(User currentUser) {
        final List<User> contacts = new ArrayList<>();

        if (currentUser.getRole() == Role.ADMIN) {
            contacts.addAll(userRepository.findAll().stream()
                    .filter(u -> !u.getId().equals(currentUser.getId()))
                    .collect(Collectors.toList()));
        } else if (currentUser.getRole() == Role.TRAINER) {
            // Add admins
            contacts.addAll(userRepository.findAll().stream()
                    .filter(u -> u.getRole() == Role.ADMIN)
                    .collect(Collectors.toList()));
            // Add clients of this trainer
            trainerRepository.findByUserId(currentUser.getId()).ifPresent(trainer -> {
                clientRepository.findAllByTrainerId(trainer.getId()).forEach(client -> {
                    contacts.add(client.getUser());
                });
            });
        } else if (currentUser.getRole() == Role.CLIENT) {
            // Add admins
            contacts.addAll(userRepository.findAll().stream()
                    .filter(u -> u.getRole() == Role.ADMIN)
                    .collect(Collectors.toList()));
            // Add assigned trainer
            clientRepository.findByUserId(currentUser.getId()).ifPresent(client -> {
                if (client.getTrainer() != null && client.getTrainer().getUser() != null) {
                    contacts.add(client.getTrainer().getUser());
                }
            });
        }

        return contacts.stream()
                .filter(User::getActive)
                .map(this::mapToProfileResponse)
                .collect(Collectors.toList());
    }

    private UserProfileResponse mapToProfileResponse(User user) {
        UserProfileResponse response = new UserProfileResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setAvatar(user.getAvatar());
        return response;
    }

    /**
     * Recupera el historial de mensajes entre dos usuarios.
     * 
     * @param currentUser Usuario autenticado.
     * @param otherUserId ID del otro participante en la conversación.
     * @return Lista de mensajes ordenados cronológicamente.
     */
    @Transactional(readOnly = true)
    public List<MessageDto> getConversation(User currentUser, Long otherUserId) {
        User otherUser = userRepository.findById(otherUserId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        validateCommunication(currentUser, otherUser);

        List<Message> messages = messageRepository.findConversation(currentUser, otherUser);
        return messages.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    /**
     * Envía un nuevo mensaje a un destinatario específico.
     * 
     * @param currentUser Usuario remitente.
     * @param request Datos del mensaje (destinatario y contenido).
     * @return El mensaje enviado en formato DTO.
     */
    @Transactional
    public MessageDto sendMessage(User currentUser, SendMessageRequest request) {
        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new IllegalArgumentException("Destinatario no encontrado"));

        validateCommunication(currentUser, receiver);

        Message message = new Message();
        message.setSender(currentUser);
        message.setReceiver(receiver);
        message.setContent(request.getContent());
        message.setTimestamp(LocalDateTime.now());

        Message saved = messageRepository.save(message);
        return mapToDto(saved);
    }

    private void validateCommunication(User currentUser, User otherUser) {
        if (currentUser.getRole() == Role.ADMIN || otherUser.getRole() == Role.ADMIN) {
            return;
        }

        if (currentUser.getRole() == Role.TRAINER && otherUser.getRole() == Role.CLIENT) {
            boolean isMyClient = clientRepository.findByUserId(otherUser.getId())
                    .map(c -> c.getTrainer() != null && c.getTrainer().getUser().getId().equals(currentUser.getId()))
                    .orElse(false);
            if (!isMyClient)
                throw new IllegalArgumentException("No puedes chatear con este cliente");
        }

        if (currentUser.getRole() == Role.CLIENT && otherUser.getRole() == Role.TRAINER) {
            boolean isMyTrainer = clientRepository.findByUserId(currentUser.getId())
                    .map(c -> c.getTrainer() != null && c.getTrainer().getUser().getId().equals(otherUser.getId()))
                    .orElse(false);
            if (!isMyTrainer)
                throw new IllegalArgumentException("No puedes chatear con este entrenador");
        }

        if (currentUser.getRole() == Role.CLIENT && otherUser.getRole() == Role.CLIENT) {
            throw new IllegalArgumentException("Los clientes no pueden chatear entre sí");
        }

        if (currentUser.getRole() == Role.TRAINER && otherUser.getRole() == Role.TRAINER) {
            throw new IllegalArgumentException("Los entrenadores no pueden chatear entre sí");
        }
    }

    private MessageDto mapToDto(Message message) {
        MessageDto dto = new MessageDto();
        dto.setId(message.getId());
        dto.setSenderId(message.getSender().getId());
        dto.setSenderName(message.getSender().getName());
        dto.setReceiverId(message.getReceiver().getId());
        dto.setReceiverName(message.getReceiver().getName());
        dto.setContent(message.getContent());
        dto.setTimestamp(message.getTimestamp());
        return dto;
    }
}
