package com.example.demo.base.config;

import com.example.demo.base.redis.RedisInfo;
import io.lettuce.core.ReadFrom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStaticMasterReplicaConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Slf4j
@Profile("ex")
@RequiredArgsConstructor
@Configuration
public class RedisProdConfig {

    private final RedisInfo info;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .readFrom(ReadFrom.REPLICA_PREFERRED)
                .build();

        RedisStaticMasterReplicaConfiguration slaveConfig =
                new RedisStaticMasterReplicaConfiguration(info.getMaster().getHost(), info.getMaster().getPort());

        info.getSlaves().forEach(slave -> slaveConfig.addNode(slave.getHost(), slave.getPort()));
        slaveConfig.setPassword(info.getMaster().getPassword());

        return new LettuceConnectionFactory(slaveConfig,clientConfig);
    }

}
