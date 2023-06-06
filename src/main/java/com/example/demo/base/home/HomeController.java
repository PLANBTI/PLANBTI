package com.example.demo.base.home;

import com.example.demo.base.security.CustomOAuth2User;
import com.example.demo.util.rq.Rq;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class HomeController {
    private final Rq rq;
    @GetMapping("/")
    public String home() {

        return "redirect:/test";
    }

    @GetMapping("/member/login")
    public String login() {
        return "member/login";
    }

    //test용 페이지 입니다.
    @GetMapping("/every")
    public String every(@RequestParam(required = false) String test) {
        if (test != null && test.equals("test"))
            return rq.redirectWithMsg("/every","안녕하세요");
        if (test != null && test.equals("back"))
            return rq.historyBack("안녕하세요");

        return "member/everyOne";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        new SecurityContextLogoutHandler()
                .logout(request, response, SecurityContextHolder.getContext().getAuthentication());

        return "redirect:/";
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
