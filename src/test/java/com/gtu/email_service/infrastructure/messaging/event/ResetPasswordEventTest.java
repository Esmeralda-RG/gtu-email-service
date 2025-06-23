package com.gtu.email_service.infrastructure.messaging.event;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.gtu.email_service.domain.model.Role;

class ResetPasswordEventTest {
    @Test
    void testResetPasswordEvent() {
        ResetPasswordEvent event = new ResetPasswordEvent();
        event.setTo("test@example.com");
        event.setRole(Role.ADMIN);
        event.setResetLink("http://example.com/reset");

        assertEquals("test@example.com", event.getTo());
        assertEquals(Role.ADMIN, event.getRole());
        assertEquals("http://example.com/reset", event.getResetLink());
    }
}
