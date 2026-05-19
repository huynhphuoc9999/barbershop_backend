package com.BaPhuocTeam.barbershop_backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
    private String message;
    private String sessionId; // Optional: để track conversation history
}
