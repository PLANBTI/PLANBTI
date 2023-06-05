/*
package com.example.demo.boundedContext.mbtiTest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class MbtiTestService {
    @Value("${chatGpt.key}")
    private String chatGptkey;

    public String sendMbtiTest(String message) {
        RestTemplate restTemplate = new RestTemplate();
        String compare = "";
        List<String> mbtiList = Arrays.asList("ISTJ", "ISTP", "ISFJ", "ISFP", "INTJ", "INTP", "INFJ", "INFP", "ESTJ", "ESTP", "ESFJ", "ESFP", "ENTJ", "ENTP", "ENFJ", "ENFP");
        for (String mbti : mbtiList) {
            if (message.contains(mbti)) {
                compare = "(이름)" + "(설명)" + "양식으로" + mbti + "에 어울리는 식물 하나만 추천해줘 \"(이름)\"은 h1 태그로 보여줘";
                break;
            }
        }
        if (!message.equals(compare)) {
            // 요청 값이 유효하지 않을 경우 에러 처리
            return "Invalid request";
        }
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

        ResponseEntity<String> responseEntity = restTemplate.exchange(httpEntity, String.class);

        String responseBody = responseEntity.getBody();
        // JSON에서 식물 이름만 추출
        String originalString = responseBody;
        String patternString = "<h1>(.*?)</h1>";

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(originalString);

        if (matcher.find()) {
            String mbtiTestResult = matcher.group(1);
            return mbtiTestResult;
        }
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


 */