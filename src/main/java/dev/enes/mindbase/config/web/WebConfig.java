package dev.enes.mindbase.config.web;

import dev.enes.mindbase.handler.AiChatWebSocketHandler;
import dev.enes.mindbase.interceptor.TenantInterceptor;
import dev.enes.mindbase.service.AiChatService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final TenantInterceptor tenantInterceptor;

    public WebConfig(TenantInterceptor tenantInterceptor) {
        this.tenantInterceptor = tenantInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tenantInterceptor).addPathPatterns("/api/**");
    }

    @Configuration
    @EnableWebSocket
    public static class WebSocketConfig implements WebSocketConfigurer {
        private final AiChatService aiChatService;
        public WebSocketConfig(AiChatService aiChatService) {
            this.aiChatService = aiChatService;
        }


        @Override
        public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
            registry.addHandler(new AiChatWebSocketHandler(aiChatService), "/ai-chat")
                    .setAllowedOriginPatterns("*");    }
    }
}
