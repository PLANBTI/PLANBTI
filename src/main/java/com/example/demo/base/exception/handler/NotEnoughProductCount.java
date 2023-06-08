package com.example.demo.base.exception.handler;

public class NotEnoughProductCount extends RuntimeException {
    public NotEnoughProductCount() {
    }

    public NotEnoughProductCount(String message) {
        super(message);
    }

    public NotEnoughProductCount(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughProductCount(Throwable cause) {
        super(cause);
    }
}
