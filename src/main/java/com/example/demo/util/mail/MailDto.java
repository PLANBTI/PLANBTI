package com.example.demo.util.mail;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MailDto {
    String email;
    String subject;
    String content;
}
