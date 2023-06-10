package com.example.demo.base.redis;

public enum RedisPrefix {
    LOGIN, BASKET, RETURN;

    public String formatKey(Long memberId) {
        return String.format("%d:%s", memberId, this.name());
    }
}