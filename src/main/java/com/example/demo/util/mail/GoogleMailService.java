package com.example.demo.util.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class GoogleMailService implements MailService {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendMail(String email, String subject, String content) {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(subject);
            message.setText(content);
            message.setTo(email);
            javaMailSender.send(message);
            log.info("mail을 {}에게 보내기 성공", email);
    }
}
