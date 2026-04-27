package com.tfgfitapp.tfgfitapp.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageRequest {
    private Long receiverId;
    private String content;
}
