package com.example.demo.base.home;

import com.example.demo.base.redis.MemberDtoRepository;
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
    private final MemberDtoRepository memberDtoRepository;
    @GetMapping("/")
    public String home() {

        return "redirect:/test";
    }

    @GetMapping("/member/login")
    public String login() {
        return "member/login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        memberDtoRepository.deleteById(rq.getMemberId());
        new SecurityContextLogoutHandler()
                .logout(request, response, SecurityContextHolder.getContext().getAuthentication());

        return "redirect:/";
    }

    @GetMapping("/about")
    public String showAbout() {
        return "about/about";
    }

}
