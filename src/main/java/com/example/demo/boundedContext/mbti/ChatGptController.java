package com.example.demo.boundedContext.mbti;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ChatGptController {

    @Value("${chatGpt.key}")
    private String chatGptkey;

//    @Value("${bard.key}")
//    private String bardKey;

    @PostMapping("/send")
    public ResponseEntity<String> send(String message) {
        RestTemplate restTemplate = new RestTemplate();

        URI uri = UriComponentsBuilder
                .fromUriString("https://api.openai.com/v1/chat/completions")
                .build()
                .encode()
                .toUri();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + chatGptkey);

        ArrayList<Message> list = new ArrayList<>();
        list.add(new Message("user", message));

        Body param = new Body("gpt-3.5-turbo", list);

        RequestEntity<Body> httpEntity = new RequestEntity<>(param, httpHeaders, HttpMethod.POST, uri);

        return restTemplate.exchange(httpEntity, String.class);
    }

    @AllArgsConstructor
    @Data
    static class Body {
        String model;
        List<Message> messages;
    }

    @AllArgsConstructor
    @Data
    static class Message {
        String role;
        String content;
    }

//    @AllArgsConstructor
//    @Data
//    static class Bard {
//        String input;
//    }
}