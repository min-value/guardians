package org.baseball.domain.Redis;

import org.redisson.api.RedissonClient;
import org.redisson.api.RBucket;
import org.springframework.stereotype.Service;

@Service
public class RedissonTestService {

    private final RedissonClient redissonClient;

    public RedissonTestService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public boolean testConnection() {
        try {
            // Redis에 "ping" 값 저장 후 가져오기 테스트
            RBucket<String> bucket = redissonClient.getBucket("pingTestKey");
            bucket.set("pong");
            String value = bucket.get();
            System.out.println("Redisson 연결 테스트 결과: " + value);
            return "pong".equals(value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
