package com.example.demo.base.exception.handler;

public class NotOwnerException extends RuntimeException {
    public NotOwnerException() {
    }

    public NotOwnerException(String message) {
        super(message);
    }

    public NotOwnerException(String message, Throwable cause) {
        super(message, cause);
    }

}
