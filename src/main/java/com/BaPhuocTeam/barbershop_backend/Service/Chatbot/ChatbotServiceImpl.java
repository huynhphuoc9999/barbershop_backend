package com.BaPhuocTeam.barbershop_backend.Service.Chatbot;

import com.BaPhuocTeam.barbershop_backend.DTO.GroqRequestDTO;
import com.BaPhuocTeam.barbershop_backend.DTO.GroqResponseDTO;
import com.BaPhuocTeam.barbershop_backend.Entity.Services;
import com.BaPhuocTeam.barbershop_backend.Entity.Shops;
import com.BaPhuocTeam.barbershop_backend.Repository.ServicesRepository;
import com.BaPhuocTeam.barbershop_backend.Repository.ShopsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class ChatbotServiceImpl implements ChatbotService {

    private static final Logger logger = LoggerFactory.getLogger(ChatbotServiceImpl.class);

    @Value("${groq.api.key}")
    private String groqApiKey;

    @Value("${groq.api.url}")
    private String groqApiUrl;

    @Value("${groq.model}")
    private String groqModel;

    @Autowired
    private ShopsRepository shopsRepository;

    @Autowired
    private ServicesRepository servicesRepository;

    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    /**
     * Build dynamic system prompt with real data from database
     */
    private String buildSystemPrompt() {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("Bạn là trợ lý ảo của tiệm cắt tóc Barbershop.\n");
        prompt.append("Nhiệm vụ của bạn:\n");
        prompt.append("- Trả lời câu hỏi về thông tin cửa hàng, dịch vụ, giá cả\n");
        prompt.append("- Hướng dẫn khách hàng đặt lịch hẹn trên website\n");
        prompt.append("- Tư vấn kiểu tóc phù hợp\n");
        prompt.append("- Trả lời ngắn gọn, thân thiện, chuyên nghiệp bằng tiếng Việt\n\n");
        
        // Get shops data
        List<Shops> shops = shopsRepository.findAll().stream()
                .filter(shop -> !shop.isDeleted())
                .limit(5)
                .collect(Collectors.toList());
        
        if (!shops.isEmpty()) {
            prompt.append("THÔNG TIN CÁC CỬA HÀNG:\n");
            for (Shops shop : shops) {
                prompt.append(String.format("- %s\n", shop.getName()));
                if (shop.getAddress() != null) {
                    prompt.append(String.format("  Địa chỉ: %s\n", shop.getAddress()));
                }
                if (shop.getPhoneNumber() != null) {
                    prompt.append(String.format("  Điện thoại: %s\n", shop.getPhoneNumber()));
                }
                if (shop.getDescription() != null && !shop.getDescription().isEmpty()) {
                    prompt.append(String.format("  Mô tả: %s\n", shop.getDescription()));
                }
            }
            prompt.append("\n");
        }
        
        // Get services data
        List<Services> services = servicesRepository.findAll().stream()
                .filter(service -> !service.isDeleted())
                .collect(Collectors.toList());
        
        if (!services.isEmpty()) {
            prompt.append("DỊCH VỤ VÀ GIÁ:\n");
            for (Services service : services) {
                String priceStr = service.getPrice() != null ? 
                    currencyFormat.format(service.getPrice()) : "Liên hệ";
                String durationStr = service.getDuration() != null ? 
                    service.getDuration() + " phút" : "";
                
                prompt.append(String.format("- %s: %s", service.getName(), priceStr));
                if (!durationStr.isEmpty()) {
                    prompt.append(String.format(" (thời gian: %s)", durationStr));
                }
                prompt.append("\n");
                
                if (service.getDescription() != null && !service.getDescription().isEmpty()) {
                    prompt.append(String.format("  %s\n", service.getDescription()));
                }
            }
            prompt.append("\n");
        }
        
        prompt.append("LƯU Ý:\n");
        prompt.append("- Khách hàng có thể đặt lịch hẹn trực tiếp trên website\n");
        prompt.append("- Nếu khách hỏi về giờ mở cửa mà không có dữ liệu, nói là 'Vui lòng liên hệ cửa hàng để biết giờ mở cửa chính xác'\n");
        prompt.append("- Luôn khuyến khích khách hàng đặt lịch online để được phục vụ nhanh chóng\n");
        
        return prompt.toString();
    }

    @Override
    public SseEmitter streamChat(String message, String sessionId) {
        SseEmitter emitter = new SseEmitter(30000L); // 30 seconds timeout

        executor.execute(() -> {
            try {
                // Build dynamic system prompt with real data
                String systemPromptContent = buildSystemPrompt();
                logger.info("System prompt built with {} characters", systemPromptContent.length());
                
                // Build Groq request with OpenAI-compatible format
                GroqRequestDTO.Message systemMessage = GroqRequestDTO.Message.builder()
                        .role("system")
                        .content(systemPromptContent)
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
