package com.gtu.email_service.infrastructure.messaging;

import com.gtu.email_service.application.service.EmailServiceImpl;
import com.gtu.email_service.infrastructure.messaging.event.ResetPasswordEvent;
import com.gtu.email_service.infrastructure.messaging.event.UserCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailMessageConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailMessageConsumer.class);
    private final EmailServiceImpl emailService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "${rabbitmq.queue.email}")
    public void receiveMessage(String message) {
        try {
            UserCreatedEvent event = objectMapper.readValue(message, UserCreatedEvent.class);
            emailService.sendWelcomeEmail(event.getEmail(), event.getUsername(), event.getPassword());
        } catch (Exception e) {
            logger.error("Failed to proccess welcome email message: {}", message, e);
        }
    }

    @RabbitListener(queues = "${rabbitmq.queue.reset}")
    public void receiveResetPasswordMessage(String message) {
        try {
            ResetPasswordEvent event = objectMapper.readValue(message, ResetPasswordEvent.class);
            emailService.sendResetEmail(event.getTo(), event.getRole(), event.getResetLink());
        } catch (Exception e) {
            logger.error("Failed to process reset password email message: {}", message, e);
        }
    }
}



