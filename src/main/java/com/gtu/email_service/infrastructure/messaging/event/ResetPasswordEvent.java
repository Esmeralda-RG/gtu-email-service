package com.gtu.email_service.infrastructure.messaging.event;

import lombok.Getter;
import lombok.Setter;

import com.gtu.email_service.domain.model.Role;

@Getter
@Setter
public class ResetPasswordEvent {
    private String to;
    private Role role;
    private String resetLink;
}
