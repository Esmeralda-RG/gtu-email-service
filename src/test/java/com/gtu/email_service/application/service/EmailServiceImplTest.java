package com.gtu.email_service.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.gtu.email_service.domain.model.Email;
import com.gtu.email_service.domain.model.Role;

public class EmailServiceImplTest {
    private JavaMailSender mailSender;
    private EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        mailSender = mock(JavaMailSender.class);
        emailService = new EmailServiceImpl(mailSender);
    }

    @Test
    void testSendEmail() {
        Email email = new Email();
        email.setTo("test@example.com");
        email.setSubject("Test Subject");
        email.setBody("Test Body");

        emailService.sendEmail(email);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());

        SimpleMailMessage captured = messageCaptor.getValue();
        assertEquals("test@example.com", captured.getTo()[0]);
        assertEquals("Test Subject", captured.getSubject());
        assertEquals("Test Body", captured.getText());
    }

    @Test
    void testSendWelcomeEmail() {
        emailService.sendWelcomeEmail("user@example.com", "username", "1234");

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage message = captor.getValue();
        assertEquals("user@example.com", message.getTo()[0]);
        assertEquals("Bienvenido a GTU - Tus Credenciales", message.getSubject());
        assertTrue(message.getText().contains("Usuario: user@example.com"));
        assertTrue(message.getText().contains("Contraseña: 1234"));
    }

    @Test
    void testSendResetEmail() {
        Role dummyRole = Role.ADMIN; 
        String resetLink = "http://example.com/reset?token=abc123";

        emailService.sendResetEmail("reset@example.com", dummyRole, resetLink);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage message = captor.getValue();
        assertEquals("reset@example.com", message.getTo()[0]);
        assertEquals("Restablecimiento de Contraseña - GTU", message.getSubject());
        assertTrue(message.getText().contains(resetLink));
        assertTrue(message.getText().contains("Este enlace expirará en 15 minutos."));
    }
}
