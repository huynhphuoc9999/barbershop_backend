package com.BaPhuocTeam.barbershop_backend.Service.Chatbot;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface ChatbotService {
    SseEmitter streamChat(String message, String sessionId);
}
