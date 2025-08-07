package org.baseball.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:db.properties")
public class RedissonConfig {
    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private int port;

    @Value("${redis.password}")
    private String password;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();

        // 단일 서버 Redis 연결
        config.useSingleServer()
                .setAddress("redis://" + host + ":" + port)
                .setPassword(password);

        // JsonJacksonCodec 명시적 설정
        config.setCodec(new JsonJacksonCodec());

        return Redisson.create(config);
    }
}
