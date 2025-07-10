package org.baseball.domain.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
@EnableScheduling
public class QueueCleanupScheduler {

    private final RedissonClient redissonClient;
    private final StringRedisTemplate redisTemplate;

    private final String QUEUE_KEY_PREFIX = "queue:";
    private final String AVAILABLE_KEY_PREFIX = "available:";

//    @Scheduled(fixedDelay = 60000)  // 1분마다 실행
//    public void cleanExpiredAvailableUsers() {
//        Set<String> keys = redisTemplate.keys(QUEUE_KEY_PREFIX + "*");
//        if (keys == null) return;
//
//        for (String queueKey : keys) {
//            RScoredSortedSet<String> queue = redissonClient.getScoredSortedSet(queueKey);
//            for (String userPk : queue) {
//                String gamePk = queueKey.substring(QUEUE_KEY_PREFIX.length());
//                String availableKey = AVAILABLE_KEY_PREFIX + gamePk + ":" + userPk;
//                Boolean isAvailable = redisTemplate.hasKey(availableKey);
//
//                if (Boolean.FALSE.equals(isAvailable)) {
//                    queue.remove(userPk);
//                    log.info("예약 가능 상태 만료: gamePk={}, userPk={}", gamePk, userPk);
//                }
//            }
//        }
//    }

    private final QueueService queueService;

    @Scheduled(fixedDelay = 3000)
    public void processQueue() {
        for (String queueKey : queueService.getAllQueueKeys()) {
            String gamePk = queueKey.substring("queue:".length());
            queueService.pollFront(gamePk);
        }
    }

}
