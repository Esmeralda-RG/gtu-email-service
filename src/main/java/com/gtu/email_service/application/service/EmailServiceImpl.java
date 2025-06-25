package com.gtu.email_service.application.service;

import com.gtu.email_service.domain.model.Email;
import com.gtu.email_service.domain.model.Role;
import com.gtu.email_service.domain.service.EmailService;
import com.gtu.email_service.infrastructure.logs.LogPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final LogPublisher logPublisher;

    @Override
    public void sendEmail(Email email) {
        try {
            if (email.getTo() == null || email.getSubject() == null || email.getBody() == null) {
                throw new IllegalArgumentException("Email fields (to, subject, or body) cannot be null");
            }
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email.getTo());
            message.setSubject(email.getSubject());
            message.setText(email.getBody());
            mailSender.send(message);

            logPublisher.sendLog(
                    Instant.now().toString(),
                    "email-service",
                    "INFO",
                    "Email sent successfully",
                    Map.of("to", email.getTo(), "subject", email.getSubject()));
        } catch (Exception e) {
            logPublisher.sendLog(
                    Instant.now().toString(),
                    "email-service",
                    "ERROR",
                    "Failed to send email",
                    Map.of("error", e.getMessage()));
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public void sendWelcomeEmail(String to, String username, String password) {
        try {
            Email email = new Email();
            email.setTo(to);
            email.setSubject("Bienvenido a GTU - Tus Credenciales");
            email.setBody(
                    "Hola " + username + ",\n\n" +
                            "Bienvenido a GTU. Aquí tienes tus credenciales:\n" +
                            "Usuario: " + to + "\n" +
                            "Contraseña: " + password + "\n\n" +
                            "¡Esperamos que disfrutes de nuestra plataforma, no olvides actualizar tu contraseña al iniciar sesión!\n\n"
                            +
                            "Saludos,\n" +
                            "Equipo de GTU");
            sendEmail(email);
        } catch (Exception e) {
            logPublisher.sendLog(
                    Instant.now().toString(),
                    "email-service",
                    "ERROR",
                    "Failed to send welcome email",
                    Map.of("error", e.getMessage()));
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public void sendResetEmail(String to, Role role, String resetLink) {
        try {
            Email email = new Email();
            email.setTo(to);
            email.setSubject("Restablecimiento de Contraseña - GTU");
            email.setBody(
                    "Hola,\n\n" +
                            "Hemos recibido una solicitud para restablecer tu contraseña. Por favor, haz clic en el enlace a continuación:\n"
                            +
                            resetLink + "\n\n" +
                            "Este enlace expirará en 15 minutos. Si no solicitaste el cambio, ignora este correo.\n\n" +
                            "Saludos,\n" +
                            "Equipo de GTU");
            sendEmail(email);
        } catch (Exception e) {
            logPublisher.sendLog(
                    Instant.now().toString(),
                    "email-service",
                    "ERROR",
                    "Failed to send reset email",
                    Map.of("error", e.getMessage()));
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
}