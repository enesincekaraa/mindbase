package dev.enes.mindbase.controller;

import dev.enes.mindbase.service.AiChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;


@RestController
public class ChatController {
    private final AiChatService aiChatService;

    public ChatController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }


    @GetMapping("/api/ask")
    public String ask(@RequestParam String question) {
        return aiChatService.askQuestion(question);
    }

}
