package com.example.demo.base.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class CustomControllerAdvice {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(TaskRejectedException.class)
    public String exceedCapacity(HttpServletRequest request, HttpServletResponse response, Exception e) {

        String referer = request.getHeader("Referer");
        log.error("TaskRejectedException 발생", e);
        log.info("TaskRejectedException 발생 | [사용자 IP] ={}", request.getRemoteAddr());

        return String.format("redirect:%s",referer);
    }
}
