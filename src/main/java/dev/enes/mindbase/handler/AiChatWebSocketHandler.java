package dev.enes.mindbase.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.enes.mindbase.service.AiChatService;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class AiChatWebSocketHandler extends TextWebSocketHandler {

    private final AiChatService aiChatService;
    private final ObjectMapper objectMapper=new ObjectMapper();

    public AiChatWebSocketHandler(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        if (!session.isOpen()) return;

        String payload = message.getPayload();
        JsonNode jsonNode = objectMapper.readTree(payload);

        String question = jsonNode.get("question").asText();
        String tenantId = jsonNode.get("tenantId").asText();

        session.sendMessage(new TextMessage("Soru işleniyor ..."));

        aiChatService.askQuestionAsync(question,tenantId).thenAccept(answer->{
            try {
                session.sendMessage(new TextMessage(answer));
            }catch (Exception e){
                System.err.println("Mesaj gönderilemedi: " + e.getMessage());
            }
        });
    }
}