package org.baseball.domain.redis;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class QueueService {

    @Autowired
    private RedissonClient redissonClient;

    private final String QUEUE_KEY_PREFIX = "queue:";
    private final String AVAILABLE_KEY_PREFIX = "available:";
    private final StringRedisTemplate redisTemplate;

    private static final int ALLOWED_ENTRANCE_COUNT = 1;
    private static final long TTL_MILLIS = 20 * 60 * 1000;

    public QueueService(RedissonClient redissonClient, StringRedisTemplate redisTemplate) {
        this.redissonClient = redissonClient;
        this.redisTemplate = redisTemplate;
    }

    public boolean enqueueUser(String gamePk, String userPk) {
        String queueKey = QUEUE_KEY_PREFIX + gamePk;
        double score = System.currentTimeMillis();

        // 모든 기존 queue에서 제거 (한 게임만 대기 허용)
        Set<String> keys = redisTemplate.keys("queue:*");
        for (String key : keys) {
            redisTemplate.opsForZSet().remove(key, String.valueOf(userPk));
            redisTemplate.delete(AVAILABLE_KEY_PREFIX + key.substring(QUEUE_KEY_PREFIX.length()) + ":" + userPk);
        }

        redisTemplate.opsForZSet().add(queueKey, String.valueOf(userPk), score);
        Long rank = redisTemplate.opsForZSet().rank(queueKey, String.valueOf(userPk));
        if (rank != null && rank < ALLOWED_ENTRANCE_COUNT) {
            redisTemplate.opsForValue().set("available:" + gamePk + ":" + userPk, "allowed", TTL_MILLIS, TimeUnit.MILLISECONDS);
        }

        return true;
    }

    //예약가능상태 확인
    public boolean canReserve(String gamePk, String userPk) {
        String queueKey = QUEUE_KEY_PREFIX + gamePk;
        String availableKey = AVAILABLE_KEY_PREFIX + gamePk + ":" + userPk;

        RScoredSortedSet<Integer> queue = redissonClient.getScoredSortedSet(queueKey);
        Integer first = queue.isEmpty() ? null : queue.first();

        if (first == null) {
            return false;
        }

        int userPkInt = Integer.parseInt(userPk);

        return userPkInt == first && Boolean.TRUE.equals(redisTemplate.hasKey(availableKey));
    }


    public boolean dequeueUser(String gamePk, String userPk) {
        String queueKey = QUEUE_KEY_PREFIX + gamePk;
        String availableKey = AVAILABLE_KEY_PREFIX + gamePk + ":" + userPk;

        Long removed = redisTemplate.opsForZSet().remove(queueKey, String.valueOf(userPk));
        redisTemplate.delete(availableKey);
        return removed != null && removed > 0;
    }

    // 대기열 순위 확인
    public Long getPosition(String gamePk, String userPk) {
        String queueKey = QUEUE_KEY_PREFIX + gamePk;
        return redisTemplate.opsForZSet().rank(queueKey, String.valueOf(userPk));
    }

    // 전체 대기열 수
    public Long getQueueSize(String gamePk) {
        String queueKey = QUEUE_KEY_PREFIX + gamePk;
        return redisTemplate.opsForZSet().zCard(queueKey);
    }

    public void pollFront(String gamePk) {
        String lockKey = "lock:pollFront:" + gamePk;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            if (lock.tryLock(5, 10, TimeUnit.SECONDS)) {
                RScoredSortedSet<String> queue = redissonClient.getScoredSortedSet(QUEUE_KEY_PREFIX + gamePk);
                String first = queue.isEmpty() ? null : queue.first();

                if (first != null) {
                    queue.remove(first);
                    // 예약 가능 상태 키 TTL 갱신
                    redisTemplate.opsForValue().set(AVAILABLE_KEY_PREFIX + gamePk + ":" + first,
                            "allowed", TTL_MILLIS, TimeUnit.MILLISECONDS);

                    log.info("예약 가능 상태 갱신: gamePk={}, userPk={}", gamePk, first);
                    notifyNext(gamePk);
                }
            } else {
                log.warn("pollFront lock 획득 실패 gamePk={}", gamePk);
            }
        } catch (InterruptedException e) {
            log.error("pollFront lock 중단", e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("pollFront 에러", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public void notifyNext(String gamePk) {
        RScoredSortedSet<String> queue = redissonClient.getScoredSortedSet(QUEUE_KEY_PREFIX + gamePk);
        String nextUser = queue.isEmpty() ? null : queue.first();
        if (nextUser != null) {
            log.info("예약 가능 유저 알림: gamePk={}, userPk={}", gamePk, nextUser);
            // 실제 알림 처리 로직 추가 가능 (ex: 메시지 큐, 웹소켓 푸시 등)
        }
    }
}
