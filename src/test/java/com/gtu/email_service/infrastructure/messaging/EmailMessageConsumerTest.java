package com.gtu.email_service.infrastructure.messaging;

import com.gtu.email_service.application.service.EmailServiceImpl;
import com.gtu.email_service.domain.model.Role;
import com.gtu.email_service.infrastructure.messaging.event.ResetPasswordEvent;
import com.gtu.email_service.infrastructure.messaging.event.UserCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class EmailMessageConsumerTest {

    @Mock
    private EmailServiceImpl emailService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private EmailMessageConsumer emailMessageConsumer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testReceiveMessage() throws Exception {
        UserCreatedEvent event = new UserCreatedEvent();
        event.setEmail("user@example.com");
        event.setUsername("username");
        event.setPassword("1234");

        when(objectMapper.readValue(anyString(), eq(UserCreatedEvent.class))).thenReturn(event);

        emailMessageConsumer.receiveMessage("{\"email\":\"user@example.com\",\"username\":\"username\",\"password\":\"1234\"}");

        verify(emailService, times(1)).sendWelcomeEmail("user@example.com", "username", "1234");
    }

    @Test
    void testReceiveMessageWithInvalidJson() throws Exception {
        when(objectMapper.readValue(anyString(), eq(UserCreatedEvent.class)))
                .thenThrow(new RuntimeException("JSON parsing error"));

        emailMessageConsumer.receiveMessage("invalid json");

        verify(emailService, never()).sendWelcomeEmail(any(), any(), any());
    }

    @Test
    void testReceiveResetPasswordMessage() throws Exception {
        ResetPasswordEvent event = new ResetPasswordEvent();
        event.setTo("reset@example.com");
        event.setRole(Role.ADMIN);
        event.setResetLink("http://example.com/reset");

        when(objectMapper.readValue(anyString(), eq(ResetPasswordEvent.class))).thenReturn(event);

        emailMessageConsumer.receiveResetPasswordMessage("{\"to\":\"reset@example.com\",\"role\":\"ADMIN\",\"resetLink\":\"http://example.com/reset\"}");

        verify(emailService, times(1)).sendResetEmail("reset@example.com", Role.ADMIN, "http://example.com/reset");
    }

    @Test
    void testReceiveResetPasswordMessageWithInvalidJson() throws Exception {
        when(objectMapper.readValue(anyString(), eq(ResetPasswordEvent.class)))
                .thenThrow(new RuntimeException("JSON parsing error"));

        emailMessageConsumer.receiveResetPasswordMessage("invalid json");

        verify(emailService, never()).sendResetEmail(any(), any(), any());
    }
}
