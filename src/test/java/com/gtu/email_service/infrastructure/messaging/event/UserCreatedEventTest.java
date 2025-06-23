package com.gtu.email_service.infrastructure.messaging.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserCreatedEventTest {

    @Test
    void testUserCreatedEvent() {
        UserCreatedEvent event = new UserCreatedEvent();
        event.setEmail("test@example.com");
        event.setUsername("username");
        event.setPassword("1234");

        assertEquals("test@example.com", event.getEmail());
        assertEquals("username", event.getUsername());
        assertEquals("1234", event.getPassword());
    }
}