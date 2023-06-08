package com.example.demo.util.rq;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseData<T> {

    private Status statusCode;
    private String msg;
    private T content;

    public static <T> ResponseData<T> of(Status statusCode, String msg, T data) {
        return new ResponseData<>(statusCode, msg, data);
    }

    public boolean isSuccess() {
        return statusCode.equals(Status.SUCCESS);
    }

    public enum Status {
        SUCCESS, FAIL
    }
}
