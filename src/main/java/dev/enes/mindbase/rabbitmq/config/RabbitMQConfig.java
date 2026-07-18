package dev.enes.mindbase.rabbitmq.config;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${RABBITMQ_QUEUE}") private String q;
    @Value("${RABBITMQ_EXCHANGE}") private String ex;
    @Value("${RABBITMQ_ROUTING_KEY}") private String rk;
    @Value("${RABBITMQ_DLQ_QUEUE}") private String dlqQ;
    @Value("${RABBITMQ_DLQ_EXCHANGE}") private String dlqEx;
    @Value("${RABBITMQ_DLQ_ROUTING_KEY}") private String dlqRk;

    public static String DOCUMENT_QUEUE, DOCUMENT_EXCHANGE, DOCUMENT_ROUTING_KEY;
    public static String DLQ_QUEUE, DLQ_EXCHANGE, DLQ_ROUTING_KEY;

    @PostConstruct
    public void init() {
        DOCUMENT_QUEUE = q; DOCUMENT_EXCHANGE = ex; DOCUMENT_ROUTING_KEY = rk;
        DLQ_QUEUE = dlqQ; DLQ_EXCHANGE = dlqEx; DLQ_ROUTING_KEY = dlqRk;
    }

    @Bean
    public Queue dlqQueue() { return new Queue(DLQ_QUEUE, true); }
    @Bean
    public DirectExchange dlqExchange() { return new DirectExchange(DLQ_EXCHANGE); }
    @Bean
    public Binding dlqBinding() { return BindingBuilder.bind(dlqQueue()).to(dlqExchange()).with(DLQ_ROUTING_KEY); }

    @Bean
    public Queue documentQueue() {
        return QueueBuilder.durable(DOCUMENT_QUEUE)
                .withArgument("x-dead-letter-exchange", DLQ_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public DirectExchange documentExchange() { return new DirectExchange(DOCUMENT_EXCHANGE); }
    @Bean
    public Binding documentBinding() { return BindingBuilder.bind(documentQueue()).to(documentExchange()).with(DOCUMENT_ROUTING_KEY); }
    @Bean
    public MessageConverter jsonMessageConverter() { return new Jackson2JsonMessageConverter(); }
}