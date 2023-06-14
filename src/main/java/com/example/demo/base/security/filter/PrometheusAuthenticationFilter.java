package com.example.demo.base.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

@Slf4j
@Component
public class PrometheusAuthenticationFilter extends OncePerRequestFilter {

    @Value("${prometheus.ip}")
    private String prometheusIp;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String ip = request.getHeader("X-Forwarded-For");

        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        boolean requestIp = request.getRequestURI().equals("/actuator/prometheus");
        boolean equalIp = ip.equals(prometheusIp);
        String requestURL = request.getRequestURL().toString();
        String remoteIP = request.getRemoteAddr();

        log.info("진짜 ip: {}",ip);
        log.info("요청 주소: {}", request.getRequestURI());
        log.info("요청한 사람의 ip: {}",request.getRemoteHost());
        log.info("요청한 사람의 port: {}",request.getRemotePort());
        log.info("주소 검증: {}", requestIp);
        log.info("ip 검증: {}",equalIp);
        log.info("요청한 사람의 통합 주소: {}",requestURL);
        log.info("remoteIP: {}",remoteIP);

        if (requestIp && equalIp) {

            Authentication authentication = new UsernamePasswordAuthenticationToken("prometheus", null, new ArrayList<>(Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"))));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("프로메테우스 통과");
            filterChain.doFilter(request, response);

        } else {
            super.doFilter(request, response, filterChain);
        }
    }




}
