package com.example.demo.util.mail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private GoogleMailService googleMailService;


    @DisplayName("이메일 전송 테스트")
    @Test
    void t1() {
        // Given
        String email = "test@example.com";
        String subject = "MbtiTest Subject";
        String content = "MbtiTest Content";

        // When
        googleMailService.sendMail(email, subject, content);

        //then
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

}