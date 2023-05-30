package com.example.demo.base.event;

import com.example.demo.util.mail.MailDto;
import com.example.demo.util.mail.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MailEventListener {

    private final MailService mailService;

    @Async("asyncMailThreadPool")
    @EventListener
    public void sendEmail(MailDto dto) {
        String email = dto.getEmail();
        String content = dto.getContent();
        String subject = dto.getSubject();
        mailService.sendMail(email,subject,content);
        log.info("{}에게 이메일 전송",email);
    }

}
