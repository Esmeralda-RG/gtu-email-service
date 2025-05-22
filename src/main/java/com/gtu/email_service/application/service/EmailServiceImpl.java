package com.gtu.email_service.application.service;

import com.gtu.email_service.domain.model.Email;
import com.gtu.email_service.domain.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(Email email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email.getTo());
        message.setSubject(email.getSubject());
        message.setText(email.getBody());
        mailSender.send(message);
    }

    public void sendWelcomeEmail(String to, String username, String password) {
        Email email = new Email();
        email.setTo(to);
        email.setSubject("Bienvenido a GTU - Tus Credenciales");
        email.setBody(
                "Hola " + username + ",\n\n" +
                "Bienvenido a GTU. Aquí tienes tus credenciales:\n" +
                "Usuario: " + to + "\n" +
                "Contraseña: " + password + "\n\n" +

                "¡Esperamos que disfrutes de nuestra plataforma, no olvides restablecer tu contraseña al iniciar sesión!\n\n" +
                "Saludos,\n" +
                "Equipo de GTU"
        );
        sendEmail(email); 
    }
}