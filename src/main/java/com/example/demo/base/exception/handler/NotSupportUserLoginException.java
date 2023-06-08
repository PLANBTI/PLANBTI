package com.example.demo.base.exception.handler;

public class NotSupportUserLoginException extends RuntimeException {
    public NotSupportUserLoginException() {
        super();
    }

    public NotSupportUserLoginException(String message) {
        super(message);
    }

    public NotSupportUserLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotSupportUserLoginException(Throwable cause) {
        super(cause);
    }
}
