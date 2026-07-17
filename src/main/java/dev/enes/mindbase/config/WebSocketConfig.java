package dev.enes.mindbase.config;

import dev.enes.mindbase.handler.AiChatWebSocketHandler;
import dev.enes.mindbase.service.AiChatService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final AiChatService  aiChatService;
    public WebSocketConfig(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new AiChatWebSocketHandler(aiChatService), "/ai-chat")
                .setAllowedOriginPatterns("*");    }
}
