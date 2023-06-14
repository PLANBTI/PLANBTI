package com.example.demo.boundedContext.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Tag(name = "MBTI",description = "MBTI 결과 및 chatGpt 요청 컨트롤러")
@Controller
public class MbtiTestController {

    @Operation(summary = "mbti test 요청")
    @GetMapping("/test")
    public String mbtiTestPage() {
        return "member/mbtiTest.html";
    }

    @Value("${chatGpt.api-key}")
    private String chatGptkey;

    @Operation(summary = "mbti test 결과 chatGpt 요청",hidden = true)
    @Parameter(name = "message",description = "mbti test 결과 메시지입니다.")
//    @Cacheable(value = "mbtiTestCache", key = "#message")
    @PostMapping("/send")
    public ResponseEntity<String> send(String message, HttpServletResponse response) {
        RestTemplate restTemplate = new RestTemplate();
        if (!isValidMBTI(message)) {
            // 요청 값이 유효하지 않을 경우 에러 처리
            return ResponseEntity.badRequest().body("Invalid request");
        }

        URI uri = UriComponentsBuilder
                .fromUriString("https://api.openai.com/v1/chat/completions")
                .build()
                .encode()
                .toUri();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + chatGptkey);

        ArrayList<MbtiTestController.Message> list = new ArrayList<>();
        list.add(new MbtiTestController.Message("user", "(이름)" + "(설명)" + "양식으로" + message + "에 어울리는 흔한 식물 하나만 추천해줘 \"(이름)\"은 h1 태그로, \"(설명)\"은 p 태그로 보여줘(\n은 제외시켜줘)"));


        Body param = new Body("gpt-3.5-turbo", list);

        RequestEntity<Body> httpEntity = new RequestEntity<>(param, httpHeaders, HttpMethod.POST, uri);

        ResponseEntity<String> responseEntity = restTemplate.exchange(httpEntity, String.class);

        String responseBody = responseEntity.getBody();

        setCookie(response, "mbti", message);

        // <h1> 태그에서 plantName 추출 후 쿠키 설정
        extractContentAndSetCookie(responseBody, response, "<h1>(.*?)</h1>", "plantName");

        // <p> 태그에서 plantDescription 추출 후 쿠키 설정
        extractContentAndSetCookie(responseBody, response, "<p>(.*?)</p>", "plantDescription");

        return responseEntity;
    }

    // MBTI 유형 검증 메소드
    private boolean isValidMBTI(String mbti) {
        String pattern = "^([IE][SN][TF][PJ])$";
        Pattern mbtiPattern = Pattern.compile(pattern);
        Matcher matcher = mbtiPattern.matcher(mbti);

        return matcher.matches();
    }

    private void extractContentAndSetCookie(String responseBody, HttpServletResponse response, String patternString, String cookieName) {
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(responseBody);

        if (matcher.find()) {
            String content = matcher.group(1);
            // 쿠키 추가
            setCookie(response, cookieName, content);
        }
    }

    // 쿠키를 생성하고 값을 설정하는 메서드
    public void setCookie(HttpServletResponse response, String name, String value) {
        try {
            String encodedValue = URLEncoder.encode(value, "UTF-8");
            Cookie cookie = new Cookie(name, encodedValue);
            response.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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

