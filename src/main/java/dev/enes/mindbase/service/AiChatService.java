package dev.enes.mindbase.service;


import dev.enes.mindbase.context.TenantContext;
import dev.enes.mindbase.dto.ChatResponse;
import dev.enes.mindbase.config.mapper.ChatMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class AiChatService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public AiChatService(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        this.chatClient = chatClientBuilder.build();
        this.vectorStore = vectorStore;
    }



    @Async
    public CompletableFuture<String> askQuestionAsync(String question,String tenantId) {
        List<Document> similarDocuments = vectorStore
                .similaritySearch(SearchRequest
                        .query(question)
                        .withTopK(1)
                        .withFilterExpression("tenantId == '" + tenantId + "'"));

        String context = similarDocuments.stream()
                .map(Document::getContent)
                .collect(Collectors.joining("\n\n"));

        String answer =  chatClient.prompt()
                .system(s -> s.text("Sadece şu bağlamı kullan: {context}").param("context", context))
                .user(question)
                .call()
                .content();

        return CompletableFuture.completedFuture(answer);
    }

    public ChatResponse askQuestion(String question) {

        String tenantId = TenantContext.getTenantId();

        List<Document> similarDocuments = vectorStore
                .similaritySearch(SearchRequest
                        .query(question)
                        .withTopK(1)
                        .withFilterExpression("tenantId == '" + tenantId + "'"));


        String context = similarDocuments.stream()
                .map(Document::getContent)
                .collect(Collectors.joining("\n\n"));

        String aiAnswer =  chatClient.prompt()
                .system(s -> s.text("Sadece şu bağlamı kullan: {context}").param("context", context))
                .user(question)
                .call()
                .content();

        return ChatMapper.toResponse(aiAnswer,tenantId);
    }

}
