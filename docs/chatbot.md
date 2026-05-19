# Chatbot Feature - Setup Guide

## 📝 Tổng quan

Tính năng chatbot sử dụng **Groq AI (Llama 3.3 70B)** - hoàn toàn FREE với streaming response real-time.

**Why Groq?**
- ✅ 100% FREE (không cần credit card)
- ✅ Rất nhanh (fastest inference speed)
- ✅ 30 requests/minute free tier
- ✅ OpenAI-compatible API
- ✅ Llama 3.3 70B model

---

## 🎯 Files Created/Modified

### Backend (Java/Spring Boot)

1. **pom.xml** - Added OkHttp 4.12.0 for streaming
2. **application.properties** - Groq config (api.key, api.url, model)
3. **DTOs:**
   - `ChatMessageDTO.java`
   - `GroqRequestDTO.java`
   - `GroqResponseDTO.java`
4. **Service:** `ChatbotService.java`, `ChatbotServiceImpl.java`
5. **Controller:** `ChatbotController.java` - `/api/chat/stream`
6. **SecurityConfig.java** - Permit `/api/chat/**`

### Frontend (React)

1. **chatbotServices.js** - SSE streaming client
2. **ChatWidget.jsx** - UI component
3. **MainLayout.jsx** - Integration

---

## 🔧 Setup Instructions

### 1. Get Groq API Key (FREE)

1. Visit: https://console.groq.com/keys
2. Sign in with Google/GitHub
3. Click **"Create API Key"**
4. Copy the key

### 2. Configure Backend

**Option A: Local (.env file)**
```bash
GROQ_API_KEY=your_api_key_here
```

**Option B: Render.com**
- Dashboard → Environment → Add Variable
- Key: `GROQ_API_KEY`
- Value: paste your key

**Option C: Docker**
```bash
docker run -e GROQ_API_KEY=your_key ...

**Option C: Docker**
```bash
docker run -e GROQ_API_KEY=your_key ...
```

### 3. Build & Run

**Backend:**
```bash
cd barbershop_backend
mvn clean install
mvn spring-boot:run
```

**Frontend:**
```bash
cd barbershop_frontend
pnpm install
pnpm dev
```

### 4. Test

```bash
# Health check
curl http://localhost:8081/api/chat/health

# Test streaming
curl -X POST http://localhost:8081/api/chat/stream \
  -H "Content-Type: application/json" \
  -d '{"message":"Giờ mở cửa là mấy giờ?","sessionId":null}'
```

---

## 🎨 Features

✅ **Real-time Streaming** - Typing effect như ChatGPT  
✅ **Floating Widget** - Button ở góc dưới phải  
✅ **Auto-scroll** - Tin nhắn mới tự động scroll  
✅ **Loading States** - Indicator khi đang chờ  
✅ **Error Handling** - Network/API errors  
✅ **Vietnamese Support** - Bot trả lời tiếng Việt

**System Prompt:**
- Trợ lý ảo Barbershop
- Tư vấn giờ mở cửa, dịch vụ, giá cả
- Hướng dẫn đặt lịch
- Ngắn gọn, thân thiện

---

## 📊 API Endpoints

**POST `/api/chat/stream`**
```json
Request: {"message": "...", "sessionId": null}
Response: SSE stream (data: text chunks)
```

**GET `/api/chat/health`**
```
Response: "Chatbot service is running"
```

---

## 🐛 Troubleshooting

| Error | Solution |
|-------|----------|
| API Error 401 | Check `GROQ_API_KEY` env variable |
| API Error 429 | Rate limit exceeded (wait 1 min) |
| Widget not showing | Check MainLayout integration |
| Streaming fails | Check CORS & backend URL |

---

## 🚀 Production Deployment

**Render.com:**
1. Add `GROQ_API_KEY` to Environment Variables
2. Deploy backend
3. Update frontend `API_URL` to production backend

**Docker:**
```dockerfile
# Set env in docker-compose.yml or run command
docker run -e GROQ_API_KEY=xxx barbershop-backend
```

---

## ✅ Checklist

- [ ] Groq API key created
- [ ] Backend builds successfully
- [ ] `/api/chat/health` returns 200
- [ ] Frontend shows chat widget
- [ ] Streaming response works
- [ ] Vietnamese text displays correctly

---

**Tech Stack:** Spring Boot + Groq AI + React + SSE Streaming  
**Model:** Llama 3.3 70B (FREE, fast, powerful)

💈✨
