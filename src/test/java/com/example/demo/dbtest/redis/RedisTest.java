package com.example.demo.dbtest.redis;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


@ActiveProfiles("test")
@Transactional
@SpringBootTest
public class RedisTest {

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @BeforeEach
    void before() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }

    @AfterEach
    void after() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }
    @Test
    void t1() {
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        String key = "user1";
        String value = "hello";
        opsForValue.set(key, value);

        String s = opsForValue.get("user1");
        Assertions.assertThat(s).isEqualTo("hello");
    }
}
