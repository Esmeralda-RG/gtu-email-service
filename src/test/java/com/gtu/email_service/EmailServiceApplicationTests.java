package com.gtu.email_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
    "spring.mail.host=localhost",
    "spring.mail.port=1025",  
    "spring.mail.username=test",
    "spring.mail.password=test"
})
class EmailServiceApplicationTests {

    @Test
    void contextLoads() {
    }
}

