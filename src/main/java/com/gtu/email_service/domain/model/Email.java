package com.gtu.email_service.domain.model;

import lombok.Data;

@Data
public class Email {
    private String to;
    private String subject;
    private String body;
}
