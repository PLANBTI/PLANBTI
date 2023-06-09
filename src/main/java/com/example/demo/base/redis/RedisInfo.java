package com.example.demo.base.redis;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Profile("prod")
@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties(prefix = "redis")
@Configuration
public class RedisInfo {
    private String host;
    private int port;
    private String password;
    private RedisInfo master;
    private List<RedisInfo> slaves;

}


