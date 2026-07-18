package dev.enes.mindbase.rabbitmq.producer;

import dev.enes.mindbase.rabbitmq.config.RabbitMQConfig;
import dev.enes.mindbase.rabbitmq.message.DocumentMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class DocumentProducer {
    private final RabbitTemplate rabbitTemplate;

    public DocumentProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendToQueue(DocumentMessage message) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.DOCUMENT_EXCHANGE,
                RabbitMQConfig.DOCUMENT_ROUTING_KEY,
                message
        );
        System.out.println("📦 RabbitMQ'ya mesaj fırlatıldı: " + message.fileName());
    }
}