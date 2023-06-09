package com.example.demo.base.exception.advice;

import com.example.demo.base.exception.handler.NotEnoughProductCount;
import com.example.demo.base.exception.handler.NotOwnerException;
import com.example.demo.base.exception.handler.OrderException;
import com.example.demo.util.rq.Rq;
import jakarta.persistence.OptimisticLockException;
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
        log.error("TaskRejectedException 발생 | [사용자 IP] ={}", request.getRemoteAddr());

        return String.format("redirect:%s",referer);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OrderException.class)
    public String badOrderRequest( Exception e) {
        return rq.historyBack(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OptimisticLockException.class)
    public String optimisticLockException( Exception e) {
        return rq.historyBack("죄송합니다 잠시후에 다시 이용해주세요.");
    }



    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(NotOwnerException.class)
    public String notOwnerException(HttpServletRequest request, Exception e) {
        log.error("NotOwnerException 발생", e);
        log.error("NotOwnerException 발생 | [사용자 IP] ={}", request.getRemoteAddr());
        return rq.redirectWithMsg("/",e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotEnoughProductCount.class)
    public String notEnoughProductCount(Exception e) {

        return rq.historyBack("재고가 부족합니다.");
    }
}
