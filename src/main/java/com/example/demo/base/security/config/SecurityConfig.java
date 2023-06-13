package com.example.demo.base.security.config;

import com.example.demo.base.security.filter.PrometheusAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Slf4j
@RequiredArgsConstructor
@EnableMethodSecurity
@EnableWebSecurity
@Configuration
public class SecurityConfig {


    private final PrometheusAuthenticationFilter filter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.oauth2Login(oauth -> oauth.loginPage("/member/login"))
                .formLogin(form -> form.loginPage("/member/login"))
                .logout(logout -> logout
                        .invalidateHttpSession(true));
        http.addFilterBefore(filter, BasicAuthenticationFilter.class);
        http.authorizeHttpRequests(auth -> auth
                //.requestMatchers("/comment/**","/adm/**").hasRole("ADMIN")
                .requestMatchers("/member/login").anonymous()
                .requestMatchers("/member/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/favicon.ico", "/resource/**", "/error",
                        "/image/**", "/js/**", "/test/**", "/send","/").permitAll()
                .anyRequest().authenticated());

        http.csrf(c -> c.ignoringRequestMatchers("/send","/test","/product/more/**"));

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
