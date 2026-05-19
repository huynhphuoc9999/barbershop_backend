package com.BaPhuocTeam.barbershop_backend.Service.Chatbot;

import com.BaPhuocTeam.barbershop_backend.DTO.GroqRequestDTO;
import com.BaPhuocTeam.barbershop_backend.DTO.GroqResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ChatbotServiceImpl implements ChatbotService {

    private static final Logger logger = LoggerFactory.getLogger(ChatbotServiceImpl.class);

    @Value("${groq.api.key}")
    private String groqApiKey;

    @Value("${groq.api.url}")
    private String groqApiUrl;

    @Value("${groq.model}")
    private String groqModel;

    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    private static final String SYSTEM_PROMPT = """
            Bạn là trợ lý ảo của tiệm cắt tóc Barbershop.
            Nhiệm vụ của bạn:
            - Trả lời câu hỏi về giờ mở cửa (8h-20h hàng ngày), dịch vụ cắt tóc, giá cả
            - Hướng dẫn khách hàng đặt lịch hẹn
            - Tư vấn kiểu tóc phù hợp
            - Trả lời ngắn gọn, thân thiện, chuyên nghiệp
            - Luôn trả lời bằng tiếng Việt
            
            Thông tin cơ bản:
            - Giờ mở cửa: 8:00 - 20:00 (Thứ 2 - Chủ nhật)
            - Dịch vụ: Cắt tóc, Nhuộm, Uốn, Gội đầu massage
            - Giá cắt tóc: Từ 50,000đ - 150,000đ
            - Đặt lịch: Khách hàng có thể đặt lịch online trên website
            """;

    @Override
    public SseEmitter streamChat(String message, String sessionId) {
        SseEmitter emitter = new SseEmitter(30000L); // 30 seconds timeout

        executor.execute(() -> {
            try {
                // Build Groq request with OpenAI-compatible format
                GroqRequestDTO.Message systemMessage = GroqRequestDTO.Message.builder()
                        .role("system")
                        .content(SYSTEM_PROMPT)
                        .build();

                GroqRequestDTO.Message userMessage = GroqRequestDTO.Message.builder()
                        .role("user")
                        .content(message)
                        .build();

                GroqRequestDTO requestDTO = GroqRequestDTO.builder()
                        .model(groqModel)
                        .messages(Arrays.asList(systemMessage, userMessage))
                        .stream(true)
                        .maxTokens(512)
                        .temperature(0.7)
                        .build();

                String requestBody = objectMapper.writeValueAsString(requestDTO);

                // Build HTTP request
                Request request = new Request.Builder()
                        .url(groqApiUrl)
                        .post(RequestBody.create(requestBody, MediaType.parse("application/json")))
                        .addHeader("Authorization", "Bearer " + groqApiKey)
                        .addHeader("Content-Type", "application/json")
                        .build();

                // Execute streaming request
                try (Response response = httpClient.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        logger.error("Groq API error: {}", response.code());
                        emitter.completeWithError(new IOException("API Error: " + response.code()));
                        return;
                    }

                    ResponseBody body = response.body();
                    if (body == null) {
                        emitter.completeWithError(new IOException("Empty response body"));
                        return;
                    }

                    // Read streaming response
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(body.byteStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (line.startsWith("data: ")) {
                                String jsonData = line.substring(6); // Remove "data: " prefix
                                
                                // Check for [DONE] signal
                                if (jsonData.trim().equals("[DONE]")) {
                                    break;
                                }
                                
                                try {
                                    GroqResponseDTO groqResponse = objectMapper.readValue(jsonData, GroqResponseDTO.class);
                                    
                                    if (groqResponse.getChoices() != null && !groqResponse.getChoices().isEmpty()) {
                                        GroqResponseDTO.Choice choice = groqResponse.getChoices().get(0);
                                        if (choice.getDelta() != null && choice.getDelta().getContent() != null) {
                                            String textChunk = choice.getDelta().getContent();
                                            if (!textChunk.isEmpty()) {
                                                // Send chunk to frontend
                                                emitter.send(SseEmitter.event()
                                                        .data(textChunk)
                                                        .name("message"));
                                            }
                                        }
                                        
                                        // Check if this is the last chunk
                                        if ("stop".equals(choice.getFinishReason())) {
                                            break;
                                        }
                                    }
                                } catch (Exception e) {
                                    logger.warn("Failed to parse chunk: {}", e.getMessage());
                                }
                            }
                        }
                    }

                    // Send completion signal
                    emitter.send(SseEmitter.event().data("[DONE]").name("done"));
                    emitter.complete();

                } catch (IOException e) {
                    logger.error("Error during streaming: ", e);
                    emitter.completeWithError(e);
                }

            } catch (Exception e) {
                logger.error("Error in chat service: ", e);
                emitter.completeWithError(e);
            }
        });

        return emitter;
    }
}
