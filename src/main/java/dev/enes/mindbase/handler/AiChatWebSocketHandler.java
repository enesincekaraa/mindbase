package dev.enes.mindbase.handler;

import dev.enes.mindbase.service.AiChatService;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class AiChatWebSocketHandler extends TextWebSocketHandler {

    private final AiChatService aiChatService;

    public AiChatWebSocketHandler(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String question = message.getPayload();

        if (!session.isOpen()) {
            System.err.println("Baglanti kapali, işlem yapilamaz.");
            return;
        }

        session.sendMessage(new TextMessage("Soru isleniyor..."));

        aiChatService.askQuestionAsync(question).thenAccept(answer -> {
            try {
                session.sendMessage(new TextMessage(answer));
            } catch (Exception e) {
                System.err.println("Mesaj gönderilemedi: " + e.getMessage());
            }
        });
    }
}