package com.gtu.email_service.domain.service;

import com.gtu.email_service.domain.model.Email;

public interface EmailService {
    void sendEmail(Email email);
}
