package com.tfgfitapp.tfgfitapp.dto;

/**
 * DTO para el envío de un mensaje directo entre usuarios.
 */
public class SendMessageRequest {

    public SendMessageRequest() {}

    public SendMessageRequest(Long receiverId, String content) {
        this.receiverId = receiverId;
        this.content = content;
    }
    private Long receiverId;
    private String content;

    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
