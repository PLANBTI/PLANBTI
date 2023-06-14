package com.example.demo.boundedContext.member.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MbtiCacheService {

    @Value("${chatGpt.api-key}")
    private String chatGptkey;

    @Cacheable(value = "mbti",key = "#message")
    public String getMbtiResult(String message) {

        log.info("캐시 설정 {} , 시간 : {}" ,message, LocalDate.now());

        RestTemplate restTemplate = new RestTemplate();

        URI uri = UriComponentsBuilder
                .fromUriString("https://api.openai.com/v1/chat/completions")
                .build()
                .encode()
                .toUri();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + chatGptkey);

        ArrayList<Message> list = new ArrayList<>();
        list.add(new Message("user", "(이름)" + "(설명)" + "양식으로" + message + "에 어울리는 흔한 식물 하나만 추천해줘 \"(이름)\"은 h1 태그로, \"(설명)\"은 p 태그로 보여줘(\n은 제외시켜줘)"));


        Body param = new Body("gpt-3.5-turbo", list);

        RequestEntity<Body> httpEntity = new RequestEntity<>(param, httpHeaders, HttpMethod.POST, uri);

        ResponseEntity<String> responseEntity = restTemplate.exchange(httpEntity, String.class);

        return responseEntity.getBody();
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

}
