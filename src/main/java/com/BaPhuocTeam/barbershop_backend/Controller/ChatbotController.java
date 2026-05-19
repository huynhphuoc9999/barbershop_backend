package com.BaPhuocTeam.barbershop_backend.Controller;

import com.BaPhuocTeam.barbershop_backend.DTO.ChatMessageDTO;
import com.BaPhuocTeam.barbershop_backend.Service.Chatbot.ChatbotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/chat")
public class ChatbotController {

    private static final Logger logger = LoggerFactory.getLogger(ChatbotController.class);

    @Autowired
    private ChatbotService chatbotService;

    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamChat(@RequestBody ChatMessageDTO chatMessageDTO) {
        logger.info("Received chat message: {}", chatMessageDTO.getMessage());
        
        String message = chatMessageDTO.getMessage();
        String sessionId = chatMessageDTO.getSessionId();
        
        if (message == null || message.trim().isEmpty()) {
            SseEmitter emitter = new SseEmitter();
            try {
                emitter.send(SseEmitter.event()
                        .data("Vui lòng nhập tin nhắn")
                        .name("error"));
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
            return emitter;
        }
        
        return chatbotService.streamChat(message, sessionId);
    }

    @GetMapping("/health")
    public String health() {
        return "Chatbot service is running";
    }
}
