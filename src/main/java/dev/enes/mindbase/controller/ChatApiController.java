package dev.enes.mindbase.controller;

import dev.enes.mindbase.dto.ChatRequest;
import dev.enes.mindbase.dto.ChatResponse;
import dev.enes.mindbase.service.AiChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai")
public class ChatApiController {

    private final AiChatService aiChatService;

    public ChatApiController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    @PostMapping("/ask")
    public ResponseEntity<ChatResponse> askQuestion(@RequestBody ChatRequest request) {
        ChatResponse response = aiChatService.askQuestion(request.question());
        return ResponseEntity.ok(response);
    }
}