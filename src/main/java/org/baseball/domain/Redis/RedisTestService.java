package org.baseball.domain.Redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RedisTestService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void performRedisTest() {
        log.info("performRedisTest() 호출 - Redis 연결 테스트 시작");
        redisTemplate.opsForValue().set("testKey", "hello");
        String value = (String) redisTemplate.opsForValue().get("testKey");
        log.info("Redis에서 가져온 값: {}", value);
    }
}