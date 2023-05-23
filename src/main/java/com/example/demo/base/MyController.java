package com.example.demo.base;

import com.example.demo.base.security.CustomOAuth2Login;
import com.example.demo.base.security.CustomOAuth2User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.example.demo.base.security.CustomOAuth2Login.*;

@Controller
public class MyController {


    @GetMapping("/")
    public String every1() {
        return "member/everyOne";
    }

    @GetMapping("/member/login")
    public String login() {
        return "member/login";
    }

    @GetMapping("/every")
    public String every() {
        return "member/everyOne";
    }


    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/member")
    public CustomOAuth2User member(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        return customOAuth2User;
    }

    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String admin() {
        return "ADMIN";
    }
}
