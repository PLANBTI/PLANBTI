package com.example.demo.base.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

@Component
public class PrometheusAuthenticationFilter extends OncePerRequestFilter {

    @Value("${prometheus.ip}")
    private String prometheusIp;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        boolean requestIp = request.getRequestURI().equals("/actuator/prometheus");
        boolean equalIp = request.getRemoteHost().equals(prometheusIp);

        if (requestIp && equalIp) {

            Authentication authentication = new UsernamePasswordAuthenticationToken("prometheus", null, new ArrayList<>(Collections.singleton(new SimpleGrantedAuthority("ADMIN"))));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } else {
            super.doFilter(request, response, filterChain);
        }
    }




}
