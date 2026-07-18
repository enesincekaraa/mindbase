package dev.enes.mindbase.rabbitmq.message;

public record DocumentMessage(
        String content,
        String fileName,
        String tenantId
){
}
