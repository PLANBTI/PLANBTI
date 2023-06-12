package com.example.demo.util.rq;

import com.example.demo.base.redis.MemberDtoRepository;
import com.example.demo.base.security.CustomOAuth2User;
import com.example.demo.boundedContext.member.dto.MemberDto;
import com.example.demo.boundedContext.member.service.MemberService;
import com.example.demo.util.ut.Ut;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

@Component
@RequestScope
public class Rq {

    private CustomOAuth2User customOAuth2User;
    private MemberDto member;
    private final LocaleResolver localeResolver;
    private Locale locale;
    private final HttpServletRequest req;
    private final HttpServletResponse resp;
    private final HttpSession session;
    private final MessageSource messageSource;
    private final MemberDtoRepository memberDtoRepository;

    public Rq(MessageSource messageSource, LocaleResolver localeResolver, HttpServletRequest req,
              HttpServletResponse resp, HttpSession session,
               MemberDtoRepository memberDtoRepository) throws JsonProcessingException {
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
        this.req = req;
        this.resp = resp;
        this.session = session;
        this.memberDtoRepository = memberDtoRepository;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof CustomOAuth2User) {
            this.customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            String username = customOAuth2User.getUsername();
            member = memberDtoRepository.findByUsername(username);
        }
    }

    public boolean isLogin() {
        return member != null;
    }

    public Long getMemberId() {
        return member.getId();
    }

    public String getUsername() {
        return member.getUsername();
    }

    public boolean isAdmin() {
        return member.getUsername().equals("admin");
    }

    public String historyBack(String msg) {
        String referer = req.getHeader("referer");
        String key = "historyBackErrorMsg___" + referer;
        req.setAttribute("localStorageKeyAboutHistoryBackErrorMsg", key);
        req.setAttribute("historyBackErrorMsg", msg);
        // 200 이 아니라 400 으로 응답코드가 지정되도록
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return "common/js";
    }

    // 뒤로가기 + 메세지
    public String historyBack(ResponseData rsData) {
        return historyBack(rsData.getMsg());
    }

    // 302 + 메세지
    public String redirectWithMsg(String url, ResponseData rsData) {
        return redirectWithMsg(url, rsData.getMsg());
    }

    // 302 + 메세지
    public String redirectWithMsg(String url, String msg) {
        return "redirect:" + urlWithMsg(url, msg);
    }

    private String urlWithMsg(String url, String msg) {
        return Ut.url.modifyQueryParam(url, "msg", msgWithTtl(msg));
    }

    private String msgWithTtl(String msg) {
        return Ut.url.encode(msg) + ";ttl=" + new Date().getTime();
    }

    public void setSessionAttr(String name, String value) {
        session.setAttribute(name, value);
    }

    public <T> T getSessionAttr(String name, T defaultValue) {
        try {
            return (T) session.getAttribute(name);
        } catch (Exception ignored) {
        }

        return defaultValue;
    }

    public void removeSessionAttr(String name) {
        session.removeAttribute(name);
    }


    public String getCText(String code, String... args) {
        return messageSource.getMessage(code, args, getLocale());
    }

    private Locale getLocale() {
        if (locale == null) locale = localeResolver.resolveLocale(req);

        return locale;
    }

    public String getParamsJsonStr() {
        Map<String, String[]> parameterMap = req.getParameterMap();

        return Ut.json.toStr(parameterMap);
    }

}
