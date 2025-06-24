package com.gtu.email_service.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.gtu.email_service.domain.model.Email;
import com.gtu.email_service.domain.model.Role;
import com.gtu.email_service.infrastructure.logs.LogPublisher;

class EmailServiceImplTest {

    private JavaMailSender mailSender;
    private EmailServiceImpl emailService;
    private LogPublisher logPublisher;

    @BeforeEach
    void setUp() {
        mailSender = mock(JavaMailSender.class);
        logPublisher = mock(LogPublisher.class);
        emailService = new EmailServiceImpl(mailSender, logPublisher);
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
    void testSendEmailWithNullValues() {
        Email email = new Email();
        assertThrows(IllegalArgumentException.class, () -> emailService.sendEmail(email),
                "Should throw exception for null or empty email fields");

        verifyNoInteractions(mailSender);
    }

    @Test
    void testSendEmailWithNullTo() {
        Email email = new Email();
        email.setSubject("Test Subject");
        email.setBody("Test Body");
        assertThrows(IllegalArgumentException.class, () -> emailService.sendEmail(email),
                "Should throw exception for null to");
        verifyNoInteractions(mailSender);
    }

    @Test
    void testSendEmailWithNullSubject() {
        Email email = new Email();
        email.setTo("test@example.com");
        email.setBody("Test Body");
        assertThrows(IllegalArgumentException.class, () -> emailService.sendEmail(email),
                "Should throw exception for null subject");
        verifyNoInteractions(mailSender);
    }

    @Test
    void testSendEmailWithNullBody() {
        Email email = new Email();
        email.setTo("test@example.com");
        email.setSubject("Test Subject");
        assertThrows(IllegalArgumentException.class, () -> emailService.sendEmail(email),
                "Should throw exception for null body");
        verifyNoInteractions(mailSender);
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
    void testSendWelcomeEmailWithNullTo() {
        assertThrows(IllegalArgumentException.class, () -> emailService.sendWelcomeEmail(null, "username", "1234"),
                "Should throw exception for null to address");
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendResetEmail() {
        String resetLink = "http://example.com/reset?token=abc123";

        emailService.sendResetEmail("reset@example.com", Role.ADMIN, resetLink);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage message = captor.getValue();
        assertEquals("reset@example.com", message.getTo()[0]);
        assertEquals("Restablecimiento de Contraseña - GTU", message.getSubject());
        assertTrue(message.getText().contains(resetLink));
        assertTrue(message.getText().contains("Este enlace expirará en 15 minutos."));
    }

    @Test
    void testSendResetEmailWithNullTo() {
        assertThrows(IllegalArgumentException.class,
                () -> emailService.sendResetEmail(null, Role.ADMIN, "http://example.com/reset"),
                "Should throw exception for null to address");
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }
}