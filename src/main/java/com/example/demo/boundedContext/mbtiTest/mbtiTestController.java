package com.example.demo.boundedContext.mbtiTest;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequiredArgsConstructor
public class mbtiTestController {
    @GetMapping("/test")
    public String mbtiTestPage() {
        return "mbtiTest/mbtiTest.html";
    }
    @GetMapping("/redirect")
    public String redirectToPage(Model model) {
        // 리다이렉트할 URL 설정
        model.addAttribute("redirectUrl", "redirect:/shop/");

        return "redirect";
    }

    @Value("${chatGpt.key}")
    private String chatGptkey;

    @PostMapping("/send")
    public ResponseEntity<String> send(String message, HttpServletResponse response) {
        RestTemplate restTemplate = new RestTemplate();

        URI uri = UriComponentsBuilder
                .fromUriString("https://api.openai.com/v1/chat/completions")
                .build()
                .encode()
                .toUri();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + chatGptkey);

        ArrayList<mbtiTestController.Message> list = new ArrayList<>();
        list.add(new mbtiTestController.Message("user", message));

        mbtiTestController.Body param = new mbtiTestController.Body("gpt-3.5-turbo", list);

        RequestEntity<mbtiTestController.Body> httpEntity = new RequestEntity<>(param, httpHeaders, HttpMethod.POST, uri);

        ResponseEntity<String> responseEntity = restTemplate.exchange(httpEntity, String.class);

        String responseBody = responseEntity.getBody();
        // JSON 식물 이름만 추출
        String originalString = responseBody;
        String patternString = "<h1>(.*?)</h1>";

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(originalString);

        if (matcher.find()) {
            String mbtiTestResult = matcher.group(1);
            System.out.println(mbtiTestResult);
            // 쿠키에 식물이름 추가
            setCookie(response, "mbtiTestResult", mbtiTestResult);
        }
        return responseEntity;
//        return restTemplate.exchange(httpEntity, String.class);
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
        List<mbtiTestController.Message> messages;
    }

    @AllArgsConstructor
    @Data
    static class Message {
        String role;
        String content;
    }

}