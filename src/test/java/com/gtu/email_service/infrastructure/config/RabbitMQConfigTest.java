package com.gtu.email_service.infrastructure.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.mail.host=localhost",
    "spring.mail.port=1025",
    "spring.mail.username=test@example.com",
    "spring.mail.password=testpass",
})
class RabbitMQConfigTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void testRabbitMQBeansAreCreated() {
        assertThat(context.getBean("emailQueue")).isNotNull();
        assertThat(context.getBean("emailExchange")).isNotNull();
        assertThat(context.getBean("emailBinding")).isNotNull();
        assertThat(context.getBean("resetQueue")).isNotNull();
        assertThat(context.getBean("resetExchange")).isNotNull();
        assertThat(context.getBean("resetBinding")).isNotNull();
    }
}