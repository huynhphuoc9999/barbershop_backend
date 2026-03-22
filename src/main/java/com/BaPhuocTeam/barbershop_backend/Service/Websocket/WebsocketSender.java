package com.BaPhuocTeam.barbershop_backend.Service.Websocket;

import com.BaPhuocTeam.barbershop_backend.DTO.FeedbackDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebsocketSender {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public void sendFeedback(FeedbackDTO feedbackDTO) {
        simpMessagingTemplate.convertAndSend("/topic/feedback/"+feedbackDTO.getBarberId(),feedbackDTO);
    }
}
