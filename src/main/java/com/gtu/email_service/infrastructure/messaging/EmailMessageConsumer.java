package com.gtu.email_service.infrastructure.messaging;

import com.gtu.email_service.application.service.EmailServiceImpl;
import com.gtu.email_service.infrastructure.messaging.event.ResetPasswordEvent;
import com.gtu.email_service.infrastructure.messaging.event.UserCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailMessageConsumer {
    
    private final EmailServiceImpl emailService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "${rabbitmq.queue.email}")
    public void receiveMessage(String message) {
        try {
            UserCreatedEvent event = objectMapper.readValue(message, UserCreatedEvent.class);
            emailService.sendWelcomeEmail(event.getEmail(), event.getUsername(), event.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "${rabbitmq.queue.reset}")
    public void receiveResetPasswordMessage(String message) {
        try {
            ResetPasswordEvent event = objectMapper.readValue(message, ResetPasswordEvent.class);
            emailService.sendResetEmail(event.getTo(), event.getRole(), event.getResetLink());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



