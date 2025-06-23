package com.gtu.email_service.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class RoleTest {
    @Test
    void testRoleValues() {
        assertEquals("SUPERADMIN", Role.SUPERADMIN.name());
        assertEquals("ADMIN", Role.ADMIN.name());
        assertEquals("DRIVER", Role.DRIVER.name());
        assertEquals("PASSENGER", Role.PASSENGER.name());
    }
}
