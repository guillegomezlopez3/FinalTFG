package com.tfgfitapp.tfgfitapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa un Mensaje en el sistema de chat interno.
 * 
 * Contiene el remitente, el destinatario, el contenido del mensaje
 * y la marca de tiempo de envío.
 */
@Entity
@Table(name = "messages")
public class Message {

    public Message() {}

    public Message(Long id, User sender, User receiver, String content, LocalDateTime timestamp) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = timestamp;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    @PrePersist
    public void prePersist() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }

    // Manual Methods
    /** @return El identificador único del mensaje. */
    public Long getId() { return id; }
    /** @param id El nuevo ID a asignar. */
    public void setId(Long id) { this.id = id; }
    /** @return El usuario que envía el mensaje. */
    public User getSender() { return sender; }
    /** @param sender El usuario remitente. */
    public void setSender(User sender) { this.sender = sender; }
    /** @return El usuario que recibe el mensaje. */
    public User getReceiver() { return receiver; }
    /** @param receiver El usuario destinatario. */
    public void setReceiver(User receiver) { this.receiver = receiver; }
    /** @return El contenido textual del mensaje. */
    public String getContent() { return content; }
    /** @param content El nuevo contenido. */
    public void setContent(String content) { this.content = content; }
    /** @return Fecha y hora de envío. */
    public LocalDateTime getTimestamp() { return timestamp; }
    /** @param timestamp La nueva marca de tiempo. */
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}
