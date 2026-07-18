package dev.enes.mindbase.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String DOCUMENT_QUEUE = "document.ingestion.queue";
    public static final String DOCUMENT_EXCHANGE = "document.exchange";
    public static final String DOCUMENT_ROUTING_KEY = "document.routing.key";

    public static final String DLQ_QUEUE = "document.dlq.queue";
    public static final String DLQ_EXCHANGE = "document.dlq.exchange";
    public static final String DLQ_ROUTING_KEY = "document.dlq.routing.key";

    // 1. DLQ (Morg Kuyruğu)
    @Bean
    public Queue dlqQueue() {
        return new Queue(DLQ_QUEUE, true);
    }

    // 2. DLQ Santrali
    @Bean
    public DirectExchange dlqExchange() {
        return new DirectExchange(DLQ_EXCHANGE);
    }

    // 3. DLQ Bağlantısı (Kuyruk ile Santrali bağlıyoruz)
    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(dlqQueue()).to(dlqExchange()).with(DLQ_ROUTING_KEY);
    }

    // 4. Ana Kuyruk (DLQ Argümanları Eklenmiş Güçlü Hali)
    @Bean
    public Queue documentQueue() {
        return QueueBuilder.durable(DOCUMENT_QUEUE)
                .withArgument("x-dead-letter-exchange", DLQ_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DLQ_ROUTING_KEY)
                .build();
    }

    // 5. Ana Santral
    @Bean
    public DirectExchange documentExchange() {
        return new DirectExchange(DOCUMENT_EXCHANGE);
    }

    // 6. Ana Bağlantı
    @Bean
    public Binding documentBinding() {
        return BindingBuilder.bind(documentQueue()).to(documentExchange()).with(DOCUMENT_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}