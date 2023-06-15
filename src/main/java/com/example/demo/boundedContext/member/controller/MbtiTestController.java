package com.example.demo.boundedContext.member.controller;

import com.example.demo.boundedContext.member.service.MbtiCacheService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "MBTI",description = "MBTI 결과 및 chatGpt 요청 컨트롤러")
@Controller
public class MbtiTestController {

    private final MbtiCacheService mbtiCacheService;

    @Operation(summary = "mbti test 요청")
    @GetMapping("/test")
    public String mbtiTestPage() {
        return "member/mbtiTest.html";
    }

    @Operation(summary = "mbti test 결과 chatGpt 요청",hidden = true)
    @Parameter(name = "message",description = "mbti test 결과 메시지입니다.")
    @PostMapping("/send")
    public ResponseEntity<String> send(String message, HttpServletResponse response, Model model) {

        if (!isValidMBTI(message)) {
            // 요청 값이 유효하지 않을 경우 에러 처리
            return ResponseEntity.badRequest().body("Invalid request");
        }

        String responseBody = mbtiCacheService.getMbtiResult(message);

        setCookie(response, "mbti", message);

        // <h1> 태그에서 plantName 추출 후 쿠키 설정
        extractContentAndSetCookie(responseBody, response, "<h1>(.*?)</h1>", "plantName");

        // <p> 태그에서 plantDescription 추출 후 쿠키 설정
        extractContentAndSetCookie(responseBody, response, "<p>(.*?)</p>", "plantDescription");

        return ResponseEntity.ok(responseBody);
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

}

