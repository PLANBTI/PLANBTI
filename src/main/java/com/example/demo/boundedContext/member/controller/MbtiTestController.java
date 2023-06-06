package com.example.demo.boundedContext.member.controller;

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

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class MbtiTestController {

    @GetMapping("/test")
    public String mbtiTestPage() {
        return "mbtiTest/mbtiTest.html";
    }
    @GetMapping("/redirect")
    public String redirectToPage(Model model) {

        model.addAttribute("redirectUrl", "redirect:/shop/");

        return "redirect";
    }

    @Value("${chatGpt.api-key}")
    private String chatGptkey;

    @Cacheable(value = "mbtiTestCache", key = "#message")
    @PostMapping("/send")
    public ResponseEntity<String> send(String message, HttpServletResponse response) {
        RestTemplate restTemplate = new RestTemplate();

        if (!checkMessage(message)) {
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
        list.add(new MbtiTestController.Message("user", message));

        MbtiTestController.Body param = new MbtiTestController.Body("gpt-3.5-turbo", list);

        RequestEntity<MbtiTestController.Body> httpEntity = new RequestEntity<>(param, httpHeaders, HttpMethod.POST, uri);

        ResponseEntity<String> responseEntity = restTemplate.exchange(httpEntity, String.class);

        String responseBody = responseEntity.getBody();
        // JSON에서 식물 이름만 추출
        String originalString = responseBody;
        String patternString = "<h1>(.*?)</h1>";

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(originalString);

        if (matcher.find()) {
            String mbtiTestResult = matcher.group(1);
            // 쿠키 결과 추가
            setCookie(response, "mbtiTestResult", mbtiTestResult);
        }
        return responseEntity;
    }
    // 유효성 검증 체크 메소드
    private boolean checkMessage(String message) {
        String pattern = "([IE][SN][TF][PJ])";
        Pattern mbtiPattern = Pattern.compile(pattern);
        Matcher matcher = mbtiPattern.matcher(message);

        String compare = "";
        if (matcher.find()) {
            String mbti = matcher.group(1);
            compare = "(이름)" + "(설명)" + "양식으로" + mbti + "에 어울리는 흔한 식물 하나만 추천해줘 \"(이름)\"은 h1 태그로 보여줘";
        }
        return message.equals(compare);
    }

    // 쿠키를 생성하고 값을 설정하는 메서드
    public void setCookie(HttpServletResponse response, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        response.addCookie(cookie);
    }

    @AllArgsConstructor
    @Data
    static class Body {
        String model;
        List<MbtiTestController.Message> messages;
    }

    @AllArgsConstructor
    @Data
    static class Message {
        String role;
        String content;
    }
}