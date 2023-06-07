package com.example.demo.base.exception;

import com.example.demo.util.rq.Rq;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class CustomControllerAdvice {

    private final Rq rq;

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(TaskRejectedException.class)
    public String exceedCapacity(HttpServletRequest request, HttpServletResponse response, Exception e) {

        String referer = request.getHeader("Referer");
        log.error("TaskRejectedException 발생", e);
        log.info("TaskRejectedException 발생 | [사용자 IP] ={}", request.getRemoteAddr());

        return String.format("redirect:%s",referer);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OrderException.class)
    public String badOrderRequest( Exception e) {
        return rq.historyBack(e.getMessage());
    }



    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotOwnerException.class)
    public String notOwnerException(HttpServletRequest request, Exception e) {
        log.error("NotOwnerException 발생", e);
        log.info("NotOwnerException 발생 | [사용자 IP] ={}", request.getRemoteAddr());
        return rq.historyBack(e.getMessage());
    }
}
