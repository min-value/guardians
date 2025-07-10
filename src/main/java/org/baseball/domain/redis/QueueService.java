package org.baseball.domain.redis;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

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
        Set<String> qkeys = redisTemplate.keys("queue:*");
        for (String key : qkeys) {
            redisTemplate.opsForZSet().remove(key, String.valueOf(userPk));
        }

        Set<String> akeys = redisTemplate.keys("available:*");
        for (String key : akeys) {
            String[] parts = key.split(":");
            if (parts.length == 3 && parts[2].equals(String.valueOf(userPk))) {
                redisTemplate.delete(key);
            }
        }

        redisTemplate.opsForZSet().add(queueKey, String.valueOf(userPk), score);

        return true;
    }

    //예약가능상태 확인
    public boolean canReserve(String gamePk, String userPk) {
        String availableKey = AVAILABLE_KEY_PREFIX + gamePk + ":" + userPk;

        return redisTemplate.hasKey(availableKey);
    }


    public boolean dequeueUser(String gamePk, String userPk) {
        String availableKey = AVAILABLE_KEY_PREFIX + gamePk + ":" + userPk;

        return redisTemplate.delete(availableKey);
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
                Set<String> availableKeys = redisTemplate.keys(AVAILABLE_KEY_PREFIX + gamePk + ":*");
                int available = availableKeys.size();
                int remaining = ALLOWED_ENTRANCE_COUNT - available;

                log.info(available + "");
                if(available >= ALLOWED_ENTRANCE_COUNT) {
                    log.info("현재 예약 가능 인원이 {}명이므로 입장 보류: gamePk={}", available, gamePk);
                    return;
                }

                log.info("진입");
                Set<String> toRemove = redisTemplate.opsForZSet().range(QUEUE_KEY_PREFIX + gamePk, 0, remaining - 1);

                if(toRemove == null || toRemove.isEmpty()) {
                    return;
                }

                for(String user : toRemove) {
                    redisTemplate.opsForZSet().remove(QUEUE_KEY_PREFIX + gamePk, user);
                    redisTemplate.opsForValue().set(AVAILABLE_KEY_PREFIX + gamePk + ":" + user, "allowed2", TTL_MILLIS, TimeUnit.MILLISECONDS);
                    log.info("예약 가능 상태 갱신: gamePk={}, userPk={}", gamePk, user);
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
        RScoredSortedSet<Integer> queue = redissonClient.getScoredSortedSet(QUEUE_KEY_PREFIX + gamePk);
        Integer nextUser = queue.isEmpty() ? null : queue.first();
        if (nextUser != null) {
            log.info("예약 가능 유저 알림: gamePk={}, userPk={}", gamePk, nextUser);
        }
    }

    public Set<String> getAllQueueKeys() {
        return redisTemplate.keys("queue:*");
    }

    public String getKey(int gamePk, int userPk) {
        return "available:" + gamePk + ":" + userPk;
    }

    public boolean checkTTL(int gamePk, int userPk) {
        String key = getKey(gamePk, userPk);
        Long ttlRemain = redisTemplate.getExpire(key);

        /*
         * 0 > ttl 남음
         * -1 = ttl 설정 없음
         * -2 = 해당 key 없음
         */
        return ttlRemain > 0;
    }
}
