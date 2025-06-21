package com.gtu.email_service.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.queue.email}")
    private String emailQueue;

    @Value("${rabbitmq.exchange.email}")
    private String emailExchange;

    @Value("${rabbitmq.routingkey.email}")
    private String emailRoutingKey;

    @Value("${rabbitmq.queue.reset}")
    private String resetQueue;

    @Value("${rabbitmq.exchange.reset}")
    private String resetExchange;

    @Value("${rabbitmq.routingkey.reset}")
    private String resetRoutingKey;

    @Bean
    public Queue emailQueue() {
        return new Queue(emailQueue, true);
    }

    @Bean
    public DirectExchange emailExchange() {
        return new DirectExchange(emailExchange);
    }

    @Bean
    public Binding emailBinding(Queue emailQueue, DirectExchange emailExchange) {
        return BindingBuilder.bind(emailQueue).to(emailExchange).with(emailRoutingKey);
    }

    @Bean
    public Queue resetQueue() {
        return new Queue(resetQueue, true);
    }

    @Bean
    public DirectExchange resetExchange() {
        return new DirectExchange(resetExchange);
    }

    @Bean
    public Binding resetBinding(Queue resetQueue, DirectExchange resetExchange) {
        return BindingBuilder.bind(resetQueue).to(resetExchange).with(resetRoutingKey);
    }
}
